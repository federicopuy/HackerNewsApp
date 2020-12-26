package com.example.hackernewsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hackernewsapp.data.model.Story

@Entity(tableName = "deleted_stories")
data class DeletedStory(@PrimaryKey val storyId: String, val deletedAt: Long)

fun Story.asDeletedStory(): DeletedStory {
    return DeletedStory(storyId, System.currentTimeMillis())
}