package com.example.hackernewsapp.fakes

import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.data.repositories.StoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : StoriesRepository {

    private val firstStory = Story("titleFirst", "", "", "", "", "")
    private val secondStory = Story("titleSecond", "", "", "", "", "")

    val storiesList = listOf(firstStory, secondStory)

    override val stories: Flow<List<Story>> = flow {
        emit(storiesList)
    }

    override suspend fun deleteStory(story: Story) {
        // Do nothing
    }
}