package com.bruno13palhano.sleeptight.ui.analytics.nap.shift

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Day
import com.bruno13palhano.sleeptight.ui.analytics.averageSleepTimeDecimal
import com.bruno13palhano.sleeptight.ui.analytics.hourToInt
import com.bruno13palhano.sleeptight.ui.analytics.isStartTimeAtNight
import com.bruno13palhano.sleeptight.ui.analytics.minuteToInt
import com.bruno13palhano.sleeptight.ui.analytics.whichDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsShiftChartViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {

    private var sundayHoursNight = mutableListOf<Int>()
    private var sundayMinutesNight = mutableListOf<Int>()
    private var sundayHoursDay = mutableListOf<Int>()
    private var sundayMinutesDay = mutableListOf<Int>()

    private var mondayHoursNight = mutableListOf<Int>()
    private var mondayMinutesNight = mutableListOf<Int>()
    private var mondayHoursDay = mutableListOf<Int>()
    private var mondayMinutesDay = mutableListOf<Int>()

    private var tuesdayHoursNight = mutableListOf<Int>()
    private var tuesdayMinutesNight = mutableListOf<Int>()
    private var tuesdayHoursDay = mutableListOf<Int>()
    private var tuesdayMinutesDay = mutableListOf<Int>()

    private var wednesdayHoursNight = mutableListOf<Int>()
    private var wednesdayMinutesNight = mutableListOf<Int>()
    private var wednesdayHoursDay = mutableListOf<Int>()
    private var wednesdayMinutesDay = mutableListOf<Int>()

    private var thursdayHoursNight = mutableListOf<Int>()
    private var thursdayMinutesNight = mutableListOf<Int>()
    private var thursdayHoursDay = mutableListOf<Int>()
    private var thursdayMinutesDay = mutableListOf<Int>()

    private var fridayHoursNight = mutableListOf<Int>()
    private var fridayMinutesNight = mutableListOf<Int>()
    private var fridayHoursDay = mutableListOf<Int>()
    private var fridayMinutesDay = mutableListOf<Int>()

    private var saturdayHoursNight = mutableListOf<Int>()
    private var saturdayMinutesNight = mutableListOf<Int>()
    private var saturdayHoursDay = mutableListOf<Int>()
    private var saturdayMinutesDay = mutableListOf<Int>()

    val shiftUi = napRepository.getAllStream()
        .map {
            it.forEach { nap->
                when (whichDay(nap.date)) {
                    Day.SUNDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = sundayHoursDay,
                            nightHours = sundayHoursNight,
                            dayMinutes = sundayMinutesDay,
                            nightMinutes = sundayMinutesNight
                        )
                    }
                    Day.MONDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = mondayHoursDay,
                            nightHours = mondayHoursNight,
                            dayMinutes = mondayMinutesDay,
                            nightMinutes = mondayMinutesNight
                        )
                    }
                    Day.TUESDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = tuesdayHoursDay,
                            nightHours = tuesdayHoursNight,
                            dayMinutes = tuesdayMinutesDay,
                            nightMinutes = tuesdayMinutesNight
                        )
                    }
                    Day.WEDNESDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = wednesdayHoursDay,
                            nightHours = wednesdayHoursNight,
                            dayMinutes = wednesdayMinutesDay,
                            nightMinutes = wednesdayMinutesNight
                        )
                    }
                    Day.THURSDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = thursdayHoursDay,
                            nightHours = thursdayHoursNight,
                            dayMinutes = thursdayMinutesDay,
                            nightMinutes = thursdayMinutesNight
                        )
                    }
                    Day.FRIDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = fridayHoursDay,
                            nightHours = fridayHoursNight,
                            dayMinutes = fridayMinutesDay,
                            nightMinutes = fridayMinutesNight
                        )
                    }
                    Day.SATURDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepTime,
                            dayHours = saturdayHoursDay,
                            nightHours = saturdayHoursNight,
                            dayMinutes = saturdayMinutesDay,
                            nightMinutes = saturdayMinutesNight
                        )
                    }
                }
            }
            ShiftChartUi(
                day = WeekDays(
                    sunday = averageSleepTimeDecimal(sundayHoursDay, sundayMinutesDay),
                    monday = averageSleepTimeDecimal(mondayHoursDay, mondayMinutesDay),
                    tuesday = averageSleepTimeDecimal(tuesdayHoursDay, tuesdayMinutesDay),
                    wednesday = averageSleepTimeDecimal(wednesdayHoursDay, wednesdayMinutesDay),
                    thursday = averageSleepTimeDecimal(thursdayHoursDay, thursdayMinutesDay),
                    friday = averageSleepTimeDecimal(fridayHoursDay, fridayMinutesDay),
                    saturday = averageSleepTimeDecimal(saturdayHoursDay, saturdayMinutesDay)
                ),
                night = WeekDays(
                    sunday = averageSleepTimeDecimal(sundayHoursNight, sundayMinutesNight),
                    monday = averageSleepTimeDecimal(mondayHoursNight, mondayMinutesNight),
                    tuesday = averageSleepTimeDecimal(tuesdayHoursNight, tuesdayMinutesNight),
                    wednesday = averageSleepTimeDecimal(wednesdayHoursNight, wednesdayMinutesNight),
                    thursday = averageSleepTimeDecimal(thursdayHoursNight, thursdayMinutesNight),
                    friday = averageSleepTimeDecimal(fridayHoursNight, fridayMinutesNight),
                    saturday = averageSleepTimeDecimal(saturdayHoursNight, saturdayMinutesNight)
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = ShiftChartUi(),
            started = WhileSubscribed(5_000)
        )

    data class ShiftChartUi(
        val day: WeekDays = WeekDays(),
        val night: WeekDays = WeekDays()
    )

    data class WeekDays(
        val sunday: Float = 0F,
        val monday: Float = 0F,
        val tuesday: Float = 0F,
        val wednesday: Float = 0F,
        val thursday: Float = 0F,
        val friday: Float = 0F,
        val saturday: Float = 0F
    )

    private fun setShiftHourAndMinute(
        startTime: Long,
        sleepTime: Long,
        dayHours: MutableList<Int>,
        nightHours: MutableList<Int>,
        dayMinutes: MutableList<Int>,
        nightMinutes: MutableList<Int>
    ) {
        if (isStartTimeAtNight(startTime)) {
            nightHours.add(hourToInt(sleepTime))
            nightMinutes.add(minuteToInt(sleepTime))
        } else {
            dayHours.add(hourToInt(sleepTime))
            dayMinutes.add(minuteToInt(sleepTime))
        }
    }
}