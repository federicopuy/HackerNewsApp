package com.example.hackernewsapp.core.cleanupstories

import androidx.annotation.VisibleForTesting
import com.example.hackernewsapp.data.local.DeletedStory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Default policy to decide if an already deleted story should be kept in memory. In this case, we
 * define a TTL. If the story was deleted before this TTL, we assume that the user does not want
 * to see this story again, and we decide to keep it in our DB. Otherwise, we purge this deleted
 * story to prevent our DeletedStoriesDB from growing until infinity and beyond.
 */
class DefaultCleanUpStoriesStrategy @Inject constructor() : CleanUpStoriesStrategy {

    override fun shouldDeleteStory(story: DeletedStory): Boolean {
        val diff = obtainCurrentTimeinMillis() - story.deletedAt
        return diff > TTL
    }

    @VisibleForTesting
    fun obtainCurrentTimeinMillis(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        private const val DAYS_TO_LIVE = 10L
        private val TTL = TimeUnit.DAYS.toMillis(DAYS_TO_LIVE)
    }
}