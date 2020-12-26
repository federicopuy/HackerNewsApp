package com.example.hackernewsapp.core.cleanupstories

import com.example.hackernewsapp.data.local.DeletedStory


interface CleanUpStoriesStrategy {

    /**
     * Defines if an already deleted story should be removed from the local DB, or if it should be
     * kept until certain conditions are met.
     * @param story the already deleted story
     *
     * @return whether the deleted story should be removed from the db.
     */
    fun shouldDeleteStory(story: DeletedStory): Boolean
}