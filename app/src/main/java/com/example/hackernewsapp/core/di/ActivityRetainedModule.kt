package com.example.hackernewsapp.core.di

import com.example.hackernewsapp.core.cleanupstories.CleanUpStoriesStrategy
import com.example.hackernewsapp.core.cleanupstories.DefaultCleanUpStoriesStrategy
import com.example.hackernewsapp.data.repositories.DefaultStoriesRepository
import com.example.hackernewsapp.data.repositories.StoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ActivityRetainedModule {

    @Binds
    abstract fun bindRepository(impl: DefaultStoriesRepository): StoriesRepository

    @Binds
    abstract fun bindCleanupStoryStrategy(impl: DefaultCleanUpStoriesStrategy): CleanUpStoriesStrategy
}