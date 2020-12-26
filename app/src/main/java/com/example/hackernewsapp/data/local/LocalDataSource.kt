package com.example.hackernewsapp.data.local

import com.example.hackernewsapp.data.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val storiesDao: StoriesDao,
    private val deletedStoriesDao: DeletedStoriesDao
) {

    fun getDeletedStories(): Flow<List<DeletedStory>> {
        return deletedStoriesDao.getDeletedStories()
    }

    fun getStoriesFromDb(): Flow<List<Story>> {
        return storiesDao.getStoriesFlow().map { it.asStoriesList() }
    }

    suspend fun saveAll(data: List<Story>) {
        storiesDao.insertAllStories(data.asStoriesEntityList())
    }

    suspend fun deleteStory(story: Story) {
        storiesDao.deleteStory(story.storyId)
        deletedStoriesDao.insertDeletedStory(story.asDeletedStory())
    }

    fun clearCache() {
        storiesDao.nukeTable()
    }

    fun removeDeletedStoryFromDb(story: DeletedStory) {
        deletedStoriesDao.deleteStory(story.storyId)
    }
}