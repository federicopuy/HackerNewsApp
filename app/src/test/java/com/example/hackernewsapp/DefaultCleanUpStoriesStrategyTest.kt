package com.example.hackernewsapp

import com.example.hackernewsapp.core.cleanupstories.DefaultCleanUpStoriesStrategy
import com.example.hackernewsapp.data.local.DeletedStory
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class DefaultCleanUpStoriesStrategyTest {

    @Spy
    lateinit var strategy: DefaultCleanUpStoriesStrategy

    @Mock
    lateinit var story: DeletedStory

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `when story was deleted less than 10 days ago, shouldDeleteStory is false`() {
        `when`(strategy.obtainCurrentTimeinMillis()).thenReturn(1608911988526)

        `when`(story.deletedAt).thenReturn(1608692400000)
        assertFalse(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(1608606000000)
        assertFalse(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(1608433200021)
        assertFalse(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(1608260405317)
        assertFalse(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(1608087603129)
        assertFalse(strategy.shouldDeleteStory(story))
    }

    @Test
    fun `when story was deleted more than 10 days ago, shouldDeleteStory is true`() {
        `when`(strategy.obtainCurrentTimeinMillis()).thenReturn(1608911988526)

        `when`(story.deletedAt).thenReturn(1608001200000)
        assertTrue(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(1607569200000)
        assertTrue(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(1578625200000)
        assertTrue(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(631936800000)
        assertTrue(strategy.shouldDeleteStory(story))

        `when`(story.deletedAt).thenReturn(789706800000)
        assertTrue(strategy.shouldDeleteStory(story))
    }
}