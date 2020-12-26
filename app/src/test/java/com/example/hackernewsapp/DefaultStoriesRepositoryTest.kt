package com.example.hackernewsapp

import com.example.hackernewsapp.core.cleanupstories.CleanUpStoriesStrategy
import com.example.hackernewsapp.data.local.DeletedStory
import com.example.hackernewsapp.data.local.LocalDataSource
import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.data.remote.RemoteDataSource
import com.example.hackernewsapp.data.repositories.DefaultStoriesRepository
import com.example.hackernewsapp.utils.MainCoroutineScopeRule
import com.example.hackernewsapp.utils.MockitoHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@FlowPreview
@ExperimentalCoroutinesApi
class DefaultStoriesRepositoryTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var repository: DefaultStoriesRepository

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var cleanUpStoriesStrategy: CleanUpStoriesStrategy

    @Mock
    lateinit var story: Story

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `deleted stories flow is emitted with the correct values`() = runBlocking {
        val deletedStories = getListOfDeletedStories()
        val deletedStoriesFlow = flow {
            emit(deletedStories)
        }
        `when`(localDataSource.getDeletedStories()).thenReturn(deletedStoriesFlow)

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        assertEquals(deletedStories, repository.deletedStories.first())
    }

    @Test
    fun `filtered stories flow is emitted with the correct values`(): Unit = runBlocking {
        val fetchedStories = getListOfStories()
        val remoteStoriesFlow = flow {
            emit(fetchedStories)
        }
        val deletedStories = getListOfDeletedStories()
        val deletedStoriesFlow = flow {
            emit(deletedStories)
        }
        `when`(remoteDataSource.fetchStories()).thenReturn(remoteStoriesFlow)
        `when`(localDataSource.getDeletedStories()).thenReturn(deletedStoriesFlow)

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))
        val filteredStories = repository.filterFetchedStories(deletedStories, fetchedStories)

        assertEquals(filteredStories, repository.filteredStories.first())
    }

    @Test
    fun `when fetching from network fails, stories from db are returned`() = runBlocking {
        val storiesFromDb = getListOfStories()
        val storiesFromDbFlow = flow { emit(storiesFromDb) }
        `when`(localDataSource.getStoriesFromDb()).thenReturn(storiesFromDbFlow)

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))
        `when`(repository.filteredStories).thenThrow(RuntimeException())

        assertEquals(storiesFromDb, repository.stories.first())
    }

    @Test
    fun `deleted stories are purged from the db when cleanUpStrategy says so`() = runBlocking {
        val deletedStories = getListOfDeletedStories()
        `when`(cleanUpStoriesStrategy.shouldDeleteStory(MockitoHelper.anyObject())).thenReturn(true)

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        repository.cleanUpDeletedStories(deletedStories)

        verify(localDataSource, times(2)).removeDeletedStoryFromDb(MockitoHelper.anyObject())
    }

    @Test
    fun `deleted stories are not purged from the db when cleanUpStrategy says so`() = runBlocking {
        val deletedStories = getListOfDeletedStories()
        `when`(cleanUpStoriesStrategy.shouldDeleteStory(MockitoHelper.anyObject())).thenReturn(false)

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        repository.cleanUpDeletedStories(deletedStories)

        verify(localDataSource, never()).removeDeletedStoryFromDb(MockitoHelper.anyObject())
    }

    @Test
    fun `deleted stories are filtered from fetched stories`() {
        val deletedStories = getListOfDeletedStories()
        val fetchedStories = getListOfStories()

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        val result = runBlocking {
            repository.filterFetchedStories(deletedStories, fetchedStories)
        }
        val resultsIds = result.map { it.storyId }.toHashSet()
        assertEquals(4, result.size)
        assertFalse(resultsIds.contains("1"))
        assertFalse(resultsIds.contains("3"))
    }

    @Test
    fun `duplicate stories are filtered from fetched stories`() {
        val fetchedStoriesWithDuplicates = listOf(
            getStoryWithId("1"),
            getStoryWithId("2"),
            getStoryWithId("3"),
            getStoryWithId("3"),
            getStoryWithId("2")
        )

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        val result = runBlocking {
            repository.filterFetchedStories(emptyList(), fetchedStoriesWithDuplicates)
        }
        val resultsIds = result.map { it.storyId }.toHashSet()

        assertEquals(3, result.size)
        assertTrue(resultsIds.contains("1"))
        assertTrue(resultsIds.contains("2"))
        assertTrue(resultsIds.contains("3"))
    }

    @Test
    fun `when deleted stories list is empty, no stories are filtered from fetched stories`() {
        val deletedStories = emptyList<DeletedStory>()
        val fetchedStories = getListOfStories()

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        val result = runBlocking {
            repository.filterFetchedStories(deletedStories, fetchedStories)
        }
        assertEquals(6, result.size)
    }

    @Test
    fun `when fetched stories list is empty, filtered stories are empty as well`() {
        val deletedStories = getListOfDeletedStories()
        val fetchedStories = emptyList<Story>()

        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        val result = runBlocking {
            repository.filterFetchedStories(deletedStories, fetchedStories)
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `when deleting a story, deleteStory in local data source is executed`() = runBlocking {
        repository = (DefaultStoriesRepository(
            localDataSource,
            remoteDataSource,
            cleanUpStoriesStrategy,
            testDispatcher
        ))

        repository.deleteStory(story)

        verify(localDataSource).deleteStory(story)
    }

    private fun getListOfStories(): List<Story> {
        return listOf(
            getStoryWithId("1"),
            getStoryWithId("2"),
            getStoryWithId("3"),
            getStoryWithId("4"),
            getStoryWithId("5"),
            getStoryWithId("6")
        )
    }

    private fun getListOfDeletedStories(): List<DeletedStory> {
        return listOf(getDeletedStoryWithId("1"), getDeletedStoryWithId("3"))
    }

    private fun getStoryWithId(id: String): Story {
        return Story("", id, "", "", "", "")
    }

    private fun getDeletedStoryWithId(id: String): DeletedStory {
        return DeletedStory(id, 1L)
    }
}