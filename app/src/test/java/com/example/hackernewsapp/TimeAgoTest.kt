package com.example.hackernewsapp

import com.example.hackernewsapp.core.utils.TimeAgo
import junit.framework.Assert.assertEquals
import org.junit.Test

class TimeAgoTest {

    @Test
    fun `post time is just now`() {
        val result = TimeAgo().getTimeAgo(1608731046418L, 1608731046418L)

        assertEquals("just now", result)
    }

    @Test
    fun `post time was a minute ago`() {
        val result = TimeAgo().getTimeAgo(1608734867000L, 1608734773000)

        assertEquals("a minute ago", result)
    }

    @Test
    fun `post time was 5 minutes ago`() {
        val result = TimeAgo().getTimeAgo(1608731046418L, 1608730744000L)

        assertEquals("5 minutes ago", result)
    }

    @Test
    fun `post time was an hour ago`() {
        val result = TimeAgo().getTimeAgo(1608734974000L, 1608731173000)

        assertEquals("an hour ago", result)
    }

    @Test
    fun `post time was hours ago`() {
        val result = TimeAgo().getTimeAgo(1608734974000L, 1608720373000)

        assertEquals("4 hours ago", result)
    }

    @Test
    fun `post time was yesterday`() {
        val result = TimeAgo().getTimeAgo(1608731046418L, 1608633973000)

        assertEquals("yesterday", result)
    }

    @Test
    fun `post time was days ago`() {
        val result = TimeAgo().getTimeAgo(1608731046418L, 1607597173000)

        assertEquals("13 days ago", result)
    }
}