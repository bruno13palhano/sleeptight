package com.bruno13palhano.sleeptight.ui.util

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.io.IOException
import kotlin.jvm.Throws

object CalendarUtil {
    fun dateToMilliseconds(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar[Calendar.DAY_OF_MONTH] = day
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year

        return calendar.timeInMillis
    }

    fun timeToMilliseconds(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute

        return calendar.timeInMillis
    }

    fun getSleepTime(startTime: Long, endTime: Long): Long {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        calendarStart.timeInMillis = startTime
        calendarEnd.timeInMillis = endTime

        val timeDiff = (calendarEnd.time.time) - (calendarStart.time.time)
        val newTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        newTime.timeInMillis = timeDiff

        return newTime.timeInMillis
    }
}

object TimePickerUtil {
    fun prepareTimePicker(time: Long): MaterialTimePicker {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return MaterialTimePicker
            .Builder()
            .setTimeFormat(CLOCK_24H)
            .setHour(calendar[Calendar.HOUR_OF_DAY])
            .setMinute(calendar[Calendar.MINUTE])
            .build()
    }
}

object DateFormatUtil {
    fun format(dateInMillis: Long): String {
        val dateFormat = SimpleDateFormat.getDateInstance()
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return dateFormat.format(dateInMillis)
    }
}

fun getHour(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time

    return calendar[Calendar.HOUR_OF_DAY]
}

fun getMinute(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time

    return calendar[Calendar.MINUTE]
}

@Throws(IOException::class)
fun getBytes(context: Context, uri: Uri): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use {
        it.buffered().readBytes()
    }
}