package com.example.hackernewsapp.data.remote

import com.example.hackernewsapp.data.model.Story
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RemoteDataSource @Inject constructor(
    private val service: StoriesApiService
) {
    fun fetchStories(): Flow<List<Story>> = flow {
        emit(service.fetchStories().hits.asStoriesList())
    }
}