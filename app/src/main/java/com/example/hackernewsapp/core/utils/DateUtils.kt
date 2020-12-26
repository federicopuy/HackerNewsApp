package com.example.hackernewsapp.core.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Obtains the string representation of how long an event occurred.
 * @param createdAt time when the event as created
 * @param format
 * @param timeZone
 *
 * @return string representation of how long an event occurred.
 */
@SuppressLint("SimpleDateFormat")
fun getFormattedDateTime(createdAt: String?, format: String, timeZone: String): String {
    if (createdAt == null) return ""

    val sdf = SimpleDateFormat(format)
    sdf.timeZone = TimeZone.getTimeZone(timeZone)

    val date = sdf.parse(createdAt)

    return TimeAgo().getTimeAgo(System.currentTimeMillis(), date.time)
}