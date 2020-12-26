package com.example.hackernewsapp.data.repositories

import com.example.hackernewsapp.data.model.Story
import kotlinx.coroutines.flow.Flow

interface StoriesRepository {

    val stories: Flow<List<Story>>

    suspend fun deleteStory(story: Story)
}