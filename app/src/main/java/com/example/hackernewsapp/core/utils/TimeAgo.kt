package com.example.hackernewsapp.core.utils

// Extracted from https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87
class TimeAgo {

    /**
     * Calculates how long ago an event occurred and returns it in string format.
     * @param currentTime in millis since epoch
     * @param time of the event, in millis since epoch
     *
     * @return the string representation of how long ago the event occurred
     */
    fun getTimeAgo(currentTime: Long, time: Long): String {
        val diff = currentTime - time

        when {
            diff < MINUTE_MILLIS -> {
                return "just now"
            }
            diff < 2 * MINUTE_MILLIS -> {
                return "a minute ago"
            }
            diff < 50 * MINUTE_MILLIS -> {
                return (diff / MINUTE_MILLIS).toString() + " minutes ago"
            }
            diff < 90 * MINUTE_MILLIS -> {
                return "an hour ago"
            }
            diff < 24 * HOUR_MILLIS -> {
                return (diff / HOUR_MILLIS).toString() + " hours ago"
            }
            diff < 48 * HOUR_MILLIS -> {
                return "yesterday"
            }
            else -> {
                return (diff / DAY_MILLIS).toString() + " days ago"
            }
        }
    }

    companion object {
        private const val SECOND_MILLIS = 1000
        const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        const val DAY_MILLIS = 24 * HOUR_MILLIS
    }
}