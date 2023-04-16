package com.bruno13palhano.sleeptight.ui.analytics

import com.bruno13palhano.model.Day
import com.bruno13palhano.model.Month
import java.util.*

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

    val durationDecimal = "$totalHours.${totalMinutes * 100 / 60}".toFloat()
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