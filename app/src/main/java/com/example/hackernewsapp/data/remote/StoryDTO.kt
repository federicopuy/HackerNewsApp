package com.example.hackernewsapp.data.remote

import com.example.hackernewsapp.core.utils.getFormattedDateTime
import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.data.remote.StoriesApiService.Companion.SERVICE_DATE_TIME_FORMAT
import com.example.hackernewsapp.data.remote.StoriesApiService.Companion.SERVICE_TIME_ZONE
import com.google.gson.annotations.SerializedName

data class StoryDTO(
    @SerializedName("story_title") val storyTitle: String?,
    @SerializedName("story_id") val storyId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("story_url") val storyUrl: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("comment_text") val commentText: String?
)

fun StoryDTO.asStory(): Story {
    return Story(
        storyTitle ?: title ?: "",
        storyId ?: "",
        storyUrl ?: "",
        getFormattedDateTime(createdAt, SERVICE_DATE_TIME_FORMAT, SERVICE_TIME_ZONE),
        author ?: "",
        commentText ?: ""
    )
}

fun List<StoryDTO>.asStoriesList(): List<Story> = map { it.asStory() }
