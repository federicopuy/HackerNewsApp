package com.example.hackernewsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hackernewsapp.data.model.Story

@Entity(tableName = "stories")
data class StoryEntity(
    val storyTitle: String,
    @PrimaryKey
    val storyId: String,
    val storyUrl: String,
    val createdAt: String,
    val author: String,
    val commentText: String
)

fun StoryEntity.asStory(): Story =
    Story(storyTitle, storyId, storyUrl, createdAt, author, commentText)

fun List<StoryEntity>.asStoriesList() = map { it.asStory() }

fun Story.asStoryEntity(): StoryEntity =
    StoryEntity(storyTitle, storyId, storyUrl, createdAt, author, commentText)

fun List<Story>.asStoriesEntityList() = map { it.asStoryEntity() }