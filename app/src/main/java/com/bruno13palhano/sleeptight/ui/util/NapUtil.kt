package com.bruno13palhano.sleeptight.ui.util

import android.icu.util.Calendar
import android.icu.util.TimeZone

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
}