package com.example.hackernewsapp.utils

import org.mockito.Mockito

/**
 * Helper used to match any() objects when these can be null
 */
object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T
}