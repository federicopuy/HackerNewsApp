package com.example.hackernewsapp.ui.flows

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.hackernewsapp.core.utils.SingleLiveEvent
import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.data.repositories.StoriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class StoriesListViewModel @ViewModelInject constructor(private val repository: StoriesRepository) :
    ViewModel() {

    @VisibleForTesting
    val clickedStoryEvent = SingleLiveEvent<String>()

    @VisibleForTesting
    val progressBarStatus = SingleLiveEvent<Boolean>()

    @VisibleForTesting
    val reloadTrigger = MutableLiveData(Unit)

    @VisibleForTesting
    val fetchedStories = repository.stories.onEach { progressBarStatus.value = false }

    var stories: LiveData<List<Story>> = Transformations.switchMap(reloadTrigger) {
        fetchedStories.asLiveData()
    }

    init {
        progressBarStatus.value = true
    }

    /**
     * Called when user swipes left on a story
     * @param story the swiped story
     */
    fun swipedStory(story: Story) {
        viewModelScope.launch {
            repository.deleteStory(story)
        }
    }

    /**
     * Called when user clicks on a row containing a story
     * @param url the redirect url of the clicked story
     */
    fun clickedStory(url: String) {
        clickedStoryEvent.value = url
    }

    /**
     * Called when user swipes screen to refresh stories
     */
    fun swipedToRefresh() {
        reloadTrigger.value = Unit
    }

}