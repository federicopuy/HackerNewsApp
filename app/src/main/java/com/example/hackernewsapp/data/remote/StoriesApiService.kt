package com.example.hackernewsapp.data.remote

import retrofit2.http.GET

interface StoriesApiService {

    @GET("search_by_date?query=android")
    suspend fun fetchStories(): StoriesListResponseDTO

    companion object {
        const val SERVICE_TIME_ZONE = "UTC"
        const val SERVICE_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}