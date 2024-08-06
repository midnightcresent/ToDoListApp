package com.example.todolistapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar

fun getTimeInMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun extractDateTimeComponents(timestamp: Long): Triple<Int, Int, Int> {
    // Convert the long timestamp to Instant
    val instant = Instant.ofEpochMilli(timestamp)

    // Convert Instant to ZonedDateTime using the system default time zone
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())

    // Extract different date and time components
    val year = zonedDateTime.year
    val month = zonedDateTime.monthValue
    val day = zonedDateTime.dayOfMonth

    return Triple(year, month, day)
}
