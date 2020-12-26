package com.example.hackernewsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.data.repositories.StoriesRepository
import com.example.hackernewsapp.fakes.FakeRepository
import com.example.hackernewsapp.ui.flows.StoriesListViewModel
import com.example.hackernewsapp.utils.MainCoroutineScopeRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@FlowPreview
@ExperimentalCoroutinesApi
class StoriesListViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private lateinit var viewModel: StoriesListViewModel

    private val fakeRepository = FakeRepository()

    @Mock
    lateinit var mockedRepository: StoriesRepository

    @Mock
    lateinit var story: Story

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `progress bar event is set to true upon initialization`() {
        viewModel = StoriesListViewModel(fakeRepository)

        assertEquals(true, viewModel.progressBarStatus.value)
    }

    @Test
    fun `flow of stories is received from repository and exposed`() = runBlocking {
        viewModel = StoriesListViewModel(fakeRepository)
        val firstEmission = viewModel.fetchedStories.first()

        assertEquals(firstEmission, fakeRepository.storiesList)
    }

    @Test
    fun `when story is swiped, deleteStory() in repository is called`() = runBlocking {
        viewModel = StoriesListViewModel(mockedRepository)
        viewModel.swipedStory(story)

        verify(mockedRepository).deleteStory(story)
    }

    @Test
    fun `when story is clicked, clicked story event is called with the correct url`() {

        viewModel = StoriesListViewModel(fakeRepository)
        val url = "someUrl"

        viewModel.clickedStory(url)

        assertEquals(url, viewModel.clickedStoryEvent.value)
    }

    @Test
    fun `when swiped to refresh, swipeToRefreshEvent is propagated`() {
        viewModel = spy(StoriesListViewModel(fakeRepository))

        viewModel.swipedToRefresh()

        assertEquals(Unit, viewModel.reloadTrigger.value)
    }
}