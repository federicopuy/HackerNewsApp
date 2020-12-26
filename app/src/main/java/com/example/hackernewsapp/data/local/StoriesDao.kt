package com.example.hackernewsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoriesDao {

    @Query("SELECT * FROM stories")
    fun getStories(): List<StoryEntity>

    @Query("SELECT * FROM stories")
    fun getStoriesFlow(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStories(stories: List<StoryEntity>)

    @Query("DELETE FROM stories")
    fun nukeTable()

    @Query("DELETE FROM stories WHERE storyId = :id")
    fun deleteStory(id: String)
}