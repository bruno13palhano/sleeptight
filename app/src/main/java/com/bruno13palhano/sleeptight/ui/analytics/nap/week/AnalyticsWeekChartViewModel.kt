package com.bruno13palhano.sleeptight.ui.analytics.nap.week

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Day
import com.bruno13palhano.sleeptight.ui.analytics.averageSleepTimeDecimal
import com.bruno13palhano.sleeptight.ui.analytics.hourToInt
import com.bruno13palhano.sleeptight.ui.analytics.minuteToInt
import com.bruno13palhano.sleeptight.ui.analytics.whichDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsWeekChartViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {
    val chartUi = napRepository.getAllStream()
        .map {
            val sundayHours = mutableListOf<Int>()
            val sundayMinutes = mutableListOf<Int>()
            val mondayHours = mutableListOf<Int>()
            val mondayMinutes = mutableListOf<Int>()
            val tuesdayHours = mutableListOf<Int>()
            val tuesdayMinute = mutableListOf<Int>()
            val wednesdayHours = mutableListOf<Int>()
            val wednesdayMinute = mutableListOf<Int>()
            val thursdayHours = mutableListOf<Int>()
            val thursdayMinute = mutableListOf<Int>()
            val fridayHours = mutableListOf<Int>()
            val fridayMinutes = mutableListOf<Int>()
            val saturdayHours = mutableListOf<Int>()
            val saturdayMinutes = mutableListOf<Int>()

            it.forEach { nap ->
                when (whichDay(nap.date)) {
                    Day.SUNDAY -> {
                        sundayHours.add(hourToInt(nap.sleepingTime))
                        sundayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.MONDAY -> {
                        mondayHours.add(hourToInt(nap.sleepingTime))
                        mondayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.TUESDAY -> {
                        tuesdayHours.add(hourToInt(nap.sleepingTime))
                        tuesdayMinute.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.WEDNESDAY -> {
                        wednesdayHours.add(hourToInt(nap.sleepingTime))
                        wednesdayMinute.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.THURSDAY -> {
                        thursdayHours.add(hourToInt(nap.sleepingTime))
                        thursdayMinute.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.FRIDAY -> {
                        fridayHours.add(hourToInt(nap.sleepingTime))
                        fridayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.SATURDAY -> {
                        saturdayHours.add(hourToInt(nap.sleepingTime))
                        saturdayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                }
            }
            WeekChartUi(
                sunday = averageSleepTimeDecimal(sundayHours, sundayMinutes),
                monday = averageSleepTimeDecimal(mondayHours, mondayMinutes),
                tuesday = averageSleepTimeDecimal(tuesdayHours, tuesdayMinute),
                wednesday = averageSleepTimeDecimal(wednesdayHours, wednesdayMinute),
                thursday = averageSleepTimeDecimal(thursdayHours, thursdayMinute),
                friday = averageSleepTimeDecimal(fridayHours, fridayMinutes),
                saturday = averageSleepTimeDecimal(saturdayHours, saturdayMinutes)
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = WeekChartUi(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    data class WeekChartUi(
        val sunday: Float = 0.0F,
        val monday: Float = 0.0F,
        val tuesday: Float = 0.0F,
        val wednesday: Float = 0.0F,
        val thursday: Float = 0.0F,
        val friday: Float = 0.0F,
        val saturday: Float = 0.0F
    )
}