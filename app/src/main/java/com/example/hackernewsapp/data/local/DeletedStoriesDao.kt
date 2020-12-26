package com.example.hackernewsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedStoriesDao {

    @Query("SELECT * FROM deleted_stories")
    fun getDeletedStories(): Flow<List<DeletedStory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedStory(story: DeletedStory)

    @Query("DELETE FROM stories WHERE storyId = :id")
    fun deleteStory(id: String)

    @Query("DELETE FROM deleted_stories")
    fun nukeTable()

}