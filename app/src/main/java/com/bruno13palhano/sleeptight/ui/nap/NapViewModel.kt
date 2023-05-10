package com.bruno13palhano.sleeptight.ui.nap

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NapViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {

    val date = MutableStateFlow(0L)
    val dateUi = date.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(year: Int, month: Int, day: Int) {
        date.value = CalendarUtil.dateToMilliseconds(year, month, day)
    }

    val startTime = MutableStateFlow(0L)
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

    val endTime = MutableStateFlow(0L)
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

    fun getNap(id: Long) {
        viewModelScope.launch {
            napRepository.getNapByIdStream(id).collect {
                title.value = it.title
                date.value = it.date
                startTime.value = it.startTime
                endTime.value = it.endTime
                observation.value = it.observation
            }
        }
    }

    fun updateNap(id: Long) {
        val nap = Nap(
            id = id,
            title = title.value,
            date = date.value,
            startTime = startTime.value,
            endTime = endTime.value,
            sleepTime = CalendarUtil.getSleepTime(startTime.value, endTime.value),
            observation = observation.value
        )
        viewModelScope.launch {
            napRepository.updateNap(nap)
        }
    }

    fun deleteNapById(id: Long) {
        viewModelScope.launch {
            napRepository.deleteNapById(id)
        }
    }
}