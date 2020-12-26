package com.example.hackernewsapp.data.model

data class Story(
    val storyTitle: String,
    val storyId: String,
    val storyUrl: String,
    val createdAt: String,
    val author: String,
    val commentText: String
)