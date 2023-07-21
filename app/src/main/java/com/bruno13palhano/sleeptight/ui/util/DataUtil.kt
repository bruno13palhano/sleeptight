package com.bruno13palhano.sleeptight.ui.util

import android.content.Context
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import java.io.IOException
import java.util.Locale
import kotlin.jvm.Throws

object CalendarUtil {
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

fun measureWithLocalDecimal(measure: String): String {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    return if (decimalSeparator == ',') {
        measure.replace(".", ",")
    } else {
        measure.replace(",", ".")
    }
}


fun stringToFloat(value: String): Float {
    return try {
        value.replace(",", ".").toFloat()
    } catch (ignored: Exception) { 0F }
}