package com.bruno13palhano.sleeptight.ui.analytics.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Month
import com.bruno13palhano.sleeptight.ui.analytics.averageSleepTimeDecimal
import com.bruno13palhano.sleeptight.ui.analytics.hourToInt
import com.bruno13palhano.sleeptight.ui.analytics.minuteToInt
import com.bruno13palhano.sleeptight.ui.analytics.whichMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsMonthChartViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {
    val chartUi = napRepository.getAllStream()
        .map {
            val januaryHours = mutableListOf<Int>()
            val januaryMinutes = mutableListOf<Int>()
            val februaryHours = mutableListOf<Int>()
            val februaryMinutes = mutableListOf<Int>()
            val marchHours = mutableListOf<Int>()
            val marchMinutes = mutableListOf<Int>()
            val aprilHours = mutableListOf<Int>()
            val aprilMinutes = mutableListOf<Int>()
            val mayHours = mutableListOf<Int>()
            val mayMinutes = mutableListOf<Int>()
            val juneHours = mutableListOf<Int>()
            val juneMinutes = mutableListOf<Int>()
            val julyHours = mutableListOf<Int>()
            val julyMinutes = mutableListOf<Int>()
            val augustHours = mutableListOf<Int>()
            val augustMinutes = mutableListOf<Int>()
            val septemberHours = mutableListOf<Int>()
            val septemberMinutes = mutableListOf<Int>()
            val octoberHours = mutableListOf<Int>()
            val octoberMinutes = mutableListOf<Int>()
            val novemberHours = mutableListOf<Int>()
            val novemberMinutes = mutableListOf<Int>()
            val decemberHours = mutableListOf<Int>()
            val decemberMinutes = mutableListOf<Int>()

            it.forEach { nap ->
                when (whichMonth(nap.date)) {
                    Month.JANUARY -> {
                        januaryHours.add(hourToInt(nap.sleepTime))
                        januaryMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.FEBRUARY -> {
                        februaryHours.add(hourToInt(nap.sleepTime))
                        februaryMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.MARCH -> {
                        marchHours.add(hourToInt(nap.sleepTime))
                        marchMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.APRIL -> {
                        aprilHours.add(hourToInt(nap.sleepTime))
                        aprilMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.MAY -> {
                        mayHours.add(hourToInt(nap.sleepTime))
                        mayMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.JUNE -> {
                        juneHours.add(hourToInt(nap.sleepTime))
                        juneMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.JULY -> {
                        julyHours.add(hourToInt(nap.sleepTime))
                        julyMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.AUGUST -> {
                        augustHours.add(hourToInt(nap.sleepTime))
                        augustMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.SEPTEMBER -> {
                        septemberHours.add(hourToInt(nap.sleepTime))
                        septemberMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.OCTOBER -> {
                        octoberHours.add(hourToInt(nap.sleepTime))
                        octoberMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.NOVEMBER -> {
                        novemberHours.add(hourToInt(nap.sleepTime))
                        novemberMinutes.add(minuteToInt(nap.sleepTime))
                    }
                    Month.DECEMBER -> {
                        decemberHours.add(hourToInt(nap.sleepTime))
                        decemberMinutes.add(minuteToInt(nap.sleepTime))
                    }
                }
            }

            MonthChartUi(
                january = averageSleepTimeDecimal(januaryHours, januaryMinutes),
                february = averageSleepTimeDecimal(februaryHours, februaryMinutes),
                march = averageSleepTimeDecimal(marchHours, marchMinutes),
                april = averageSleepTimeDecimal(aprilHours, aprilMinutes),
                may = averageSleepTimeDecimal(mayHours, mayMinutes),
                june = averageSleepTimeDecimal(juneHours, juneMinutes),
                july = averageSleepTimeDecimal(julyHours, julyMinutes),
                august = averageSleepTimeDecimal(augustHours, augustMinutes),
                september = averageSleepTimeDecimal(septemberHours, septemberMinutes),
                october = averageSleepTimeDecimal(octoberHours, octoberMinutes),
                november = averageSleepTimeDecimal(novemberHours, novemberMinutes),
                december = averageSleepTimeDecimal(decemberHours, decemberMinutes)
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = MonthChartUi(),
            started = WhileSubscribed(5_000)
        )

    data class MonthChartUi(
        val january: Float =  0.0F,
        val february: Float = 0.0F,
        val march: Float = 0.0F,
        val april: Float = 0.0F,
        val may: Float = 0.0F,
        val june: Float = 0.0F,
        val july: Float = 0.0F,
        val august: Float = 0.0F,
        val september: Float = 0.0F,
        val october: Float = 0.0F,
        val november: Float = 0.0F,
        val december: Float = 0.0F
    )
}