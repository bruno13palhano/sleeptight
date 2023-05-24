package com.bruno13palhano.sleeptight.ui.analytics.nap

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsNapChartViewModel @Inject constructor(
    @DefaultNapRep napRepository: NapRepository
) : ViewModel() {

    val napChartUi = napRepository.getAllStream()
        .map {
            val allSleepTime = mutableListOf<Float>()
            val allDate = mutableListOf<String>()

            it.forEach { nap ->
                allSleepTime.add(timeToDecimal(nap.sleepTime))
                allDate.add(DateFormatUtil.format(nap.date))
            }

            NapChartUi(
                allSleeptime = allSleepTime,
                allDate = allDate
            )

        }
        .stateIn(
            scope = viewModelScope,
            initialValue = NapChartUi(),
            started = WhileSubscribed(5_000)
        )

    data class NapChartUi(
        val allSleeptime: List<Float> = emptyList(),
        val allDate: List<String> = emptyList()
    )

    private fun timeToDecimal(time: Long): Float {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = time

        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val finalMinutes: String
        val currentMinutes = minute * 100 / 60
        finalMinutes = if (currentMinutes < 10) {
            "0${currentMinutes}"
        } else {
            currentMinutes.toString()
        }

        val timeDecimal = "$hour.$finalMinutes".toFloat()
        return if (timeDecimal == 0.0F) 0.0F else String.format("%.2f", timeDecimal)
            .replace(",", ".").toFloat()
    }
}