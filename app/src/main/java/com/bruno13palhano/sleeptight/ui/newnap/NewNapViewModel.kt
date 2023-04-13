package com.bruno13palhano.sleeptight.ui.newnap

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Nap
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewNapViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {
    private val currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    val date = MutableStateFlow(currentDate.timeInMillis)
    val dateUi = date.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.YEAR_ABBR_MONTH_WEEKDAY_DAY).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(year: Int, month: Int, day: Int) {
        date.value = dateToMilliseconds(year, month, day)
    }

    val startTime = MutableStateFlow(currentDate.timeInMillis)
    val startTimeUi = startTime.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setStartTime(hour: Int, minute: Int) {
        startTime.value = timeToMilliseconds(hour, minute)
    }

    val endTime = MutableStateFlow(currentDate.timeInMillis)
    val endTimeUi = endTime.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setEndTime(hour: Int, minute: Int) {
        endTime.value = timeToMilliseconds(hour, minute)
    }

    val _observation = MutableStateFlow("")
    val observation = _observation.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun insertNap(nap: Nap) {
        viewModelScope.launch {
            napRepository.insert(nap)
        }
    }

    private fun dateToMilliseconds(year: Int, month: Int, day: Int): Long {
        val calendar = android.icu.util.Calendar.getInstance(android.icu.util.TimeZone.getTimeZone("UTC"))
        calendar[android.icu.util.Calendar.DAY_OF_MONTH] = day
        calendar[android.icu.util.Calendar.MONTH] = month
        calendar[android.icu.util.Calendar.YEAR] = year

        return calendar.timeInMillis
    }

    private fun timeToMilliseconds(hour: Int, minute: Int): Long {
        val calendar = android.icu.util.Calendar.getInstance()
        calendar[android.icu.util.Calendar.HOUR_OF_DAY] = hour
        calendar[android.icu.util.Calendar.MINUTE] = minute

        return calendar.timeInMillis
    }
}