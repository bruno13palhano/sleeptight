package com.bruno13palhano.sleeptight.ui.lists.nap.newnap

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNapViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {
    private val currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    val date = MutableStateFlow(MaterialDatePicker.todayInUtcMilliseconds())
    val dateUi = date.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(date: Long) {
        this.date.value = date
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
        startTime.value = CalendarUtil.timeToMilliseconds(hour, minute)
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
        endTime.value = CalendarUtil.timeToMilliseconds(hour, minute)
    }

    val title = MutableStateFlow("")
    val observation = MutableStateFlow("")

    fun insertNap() {
        viewModelScope.launch {
            val nap = Nap(
                id = 0L,
                title = title.value,
                date = date.value,
                startTime = startTime.value,
                endTime = endTime.value,
                sleepTime = CalendarUtil.getSleepTime(startTime.value, endTime.value),
                observation = observation.value
            )
            napRepository.insert(nap)
        }
        restoreValues()
    }

    private fun restoreValues() {
        date.value = MaterialDatePicker.todayInUtcMilliseconds()
        title.value = ""
        startTime.value = currentDate.timeInMillis
        endTime.value = currentDate.timeInMillis
        observation.value = ""
    }
}