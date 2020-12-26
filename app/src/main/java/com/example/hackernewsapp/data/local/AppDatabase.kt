package com.example.hackernewsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StoryEntity::class, DeletedStory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storiesDao(): StoriesDao

    abstract fun deletedStoriesDao(): DeletedStoriesDao
}