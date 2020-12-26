package com.example.hackernewsapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.hackernewsapp.core.cleanupstories.DefaultCleanUpStoriesStrategy
import com.example.hackernewsapp.data.local.AppDatabase
import com.example.hackernewsapp.data.local.DeletedStoriesDao
import com.example.hackernewsapp.data.local.LocalDataSource
import com.example.hackernewsapp.data.local.StoriesDao
import com.example.hackernewsapp.data.remote.RemoteDataSource
import com.example.hackernewsapp.data.remote.StoriesApiService
import com.example.hackernewsapp.data.repositories.DefaultStoriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    private const val BASE_URL = "https://hn.algolia.com/api/v1/"
    private const val DATABASE_NAME = "NEWS_DB"

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun provideStoriesRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        cleanUpStoriesStrategy: DefaultCleanUpStoriesStrategy
    ): DefaultStoriesRepository {
        return DefaultStoriesRepository(localDataSource, remoteDataSource, cleanUpStoriesStrategy)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideStoriesDao(database: AppDatabase): StoriesDao {
        return database.storiesDao()
    }

    @Provides
    @Singleton
    fun provideDeletedStoriesDao(database: AppDatabase): DeletedStoriesDao {
        return database.deletedStoriesDao()
    }

    @Provides
    @Singleton
    fun provideService(okHttpClient: OkHttpClient): StoriesApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(StoriesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )

        ).build()
    }
}