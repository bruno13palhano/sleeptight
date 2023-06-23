package com.bruno13palhano.sleeptight.ui.screens.analytics

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Images.Media.IS_PENDING
import android.view.View
import com.bruno13palhano.model.Day
import com.bruno13palhano.model.Month
import okio.use

fun averageSleepTimeDecimal(hours: List<Int>, minutes: List<Int>): Float {
    var totalHours = hours.sum()
    var totalMinutes = 0

    minutes.forEach {
        totalMinutes += it
        if (totalMinutes >= 60) {
            totalHours++
            totalMinutes -= 60
        }
    }

    val finalMinutes: String
    val currentMinutes = totalMinutes * 100 / 60
    finalMinutes = if (currentMinutes < 10) {
        "0${currentMinutes}"
    } else {
        currentMinutes.toString()
    }

    val durationDecimal = "$totalHours.$finalMinutes".toFloat()
    return if (durationDecimal == 0.0F) 0.0F else String.format("%.2f", durationDecimal/hours.size)
        .replace(",", ".").toFloat()
}

fun hourToInt(time: Long): Int {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = time

    return calendar[Calendar.HOUR_OF_DAY]
}

fun minuteToInt(time: Long): Int {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = time

    return calendar[Calendar.MINUTE]
}

fun whichDay(date: Long): Day {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = date

    return when (calendar[Calendar.DAY_OF_WEEK]) {
        1 -> {
            Day.SUNDAY
        }
        2 -> {
            Day.MONDAY
        }
        3 -> {
            Day.TUESDAY
        }
        4 -> {
            Day.WEDNESDAY
        }
        5 -> {
            Day.THURSDAY
        }
        6 -> {
            Day.FRIDAY
        }
        else -> {
            Day.SATURDAY
        }
    }
}

fun whichMonth(date: Long): Month {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = date

    return when (calendar[Calendar.MONTH]) {
        0 -> {
            Month.JANUARY
        }
        1 -> {
            Month.FEBRUARY
        }
        2 -> {
            Month.MARCH
        }
        3 -> {
            Month.APRIL
        }
        4 -> {
            Month.MAY
        }
        5 -> {
            Month.JUNE
        }
        6 -> {
            Month.JULY
        }
        7 -> {
            Month.AUGUST
        }
        8 -> {
            Month.SEPTEMBER
        }
        9 -> {
            Month.OCTOBER
        }
        10 -> {
            Month.NOVEMBER
        }
        else -> {
            Month.DECEMBER
        }
    }
}

fun isStartTimeAtNight(startTime: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = startTime

    val hour = calendar[Calendar.HOUR_OF_DAY]
    return hour <= 5 || hour >= 18
}

fun chartScreenshot(view: View, height: Int, width: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val bgDrawable = view.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }

    view.draw(canvas)
    return bitmap
}

fun shareChart(
    context: Context,
    chartName: String,
    view: View,
    height: Int,
    width: Int
) {
    val shareChartImage = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, getImageUri(context, chartScreenshot(view, height, width), chartName))
        putExtra(Intent.EXTRA_TITLE, chartName)
    }, null)
    context.startActivity(shareChartImage)
}

fun getImageUri(context: Context, imageBitmap: Bitmap, filename: String): Uri {
    val fn = filename.replace(" ", "_")
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}_${fn}.png")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
        put(MediaStore.MediaColumns.IS_PENDING, 0)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(EXTERNAL_CONTENT_URI, contentValues)
    uri?.let { resolver.openOutputStream(it) }
        ?.use {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.flush()
            it.close()
        }

    contentValues.clear()
    contentValues.put(IS_PENDING, 0)
    uri?.let {
        resolver.update(it, contentValues, null, null)
    }

    return uri!!
}