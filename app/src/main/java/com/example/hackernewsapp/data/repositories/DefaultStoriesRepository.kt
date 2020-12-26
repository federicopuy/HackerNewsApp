package com.example.hackernewsapp.data.repositories

import androidx.annotation.AnyThread
import androidx.annotation.VisibleForTesting
import com.example.hackernewsapp.core.cleanupstories.CleanUpStoriesStrategy
import com.example.hackernewsapp.data.local.DeletedStory
import com.example.hackernewsapp.data.local.LocalDataSource
import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.data.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultStoriesRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource,
    private val cleanUpStoriesStrategy: CleanUpStoriesStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : StoriesRepository {

    /**
     * Stream of stories that the user has deleted in the past. On every iteration, we purge
     * stories if there is no point in keeping them, such as if they were deleted long time ago.
     */
    @VisibleForTesting
    val deletedStories = localDataSource.getDeletedStories()
        .onEach {
            cleanUpDeletedStories(it)
        }

    /**
     * Fetched stories are filtered in order to avoid showing an already deleted story to the user
     */
    @VisibleForTesting
    val filteredStories = remoteDataSource.fetchStories()
        .combine(deletedStories) { storiesFromRemote, deletedStories ->
            filterFetchedStories(deletedStories, storiesFromRemote)
        }

    /**
     * Flow of already filtered stories. On each iteration, we clear the cache and replace it with
     * the new values, and in case the fetching of the data from the remoteDataSource fails, we
     * return the previously cached values.
     */
    override val stories = filteredStories
        .onEach {
            localDataSource.clearCache()
            localDataSource.saveAll(it)
        }
        .catch {
            emitAll(localDataSource.getStoriesFromDb())
        }
        .flowOn(dispatcher)
        .conflate()

    @VisibleForTesting
    @AnyThread
    suspend fun cleanUpDeletedStories(deletedStories: List<DeletedStory>) {
        withContext(dispatcher) {
            for (deletedStory in deletedStories) {
                if (cleanUpStoriesStrategy.shouldDeleteStory(deletedStory)) {
                    localDataSource.removeDeletedStoryFromDb(deletedStory)
                }
            }
        }
    }

    /**
     * Receives a list of fetched stories and removes the ones that have been deleted by the user
     * in the past, as well as the duplicate stories.
     *
     * @param deletedStories the stories that have been already deleted by the user
     * @param fetchedStories the stories retrieved from the service
     *
     * @return stories without duplicates and previously deleted stories
     */
    @VisibleForTesting
    @AnyThread
    suspend fun filterFetchedStories(
        deletedStories: List<DeletedStory>,
        fetchedStories: List<Story>
    ): List<Story> = withContext(dispatcher) {
        val deletedStoriesIds = deletedStories.map { it.storyId }.toHashSet()
        val filteredStories = hashMapOf<String, Story>()

        for (story in fetchedStories) {
            if (!deletedStoriesIds.contains(story.storyId) &&
                !filteredStories.containsKey(story.storyId)
            ) {
                filteredStories[story.storyId] = story
            }
        }
        filteredStories.values.toList()
    }

    @AnyThread
    override suspend fun deleteStory(story: Story) {
        withContext(dispatcher) {
            localDataSource.deleteStory(story)
        }
    }

}