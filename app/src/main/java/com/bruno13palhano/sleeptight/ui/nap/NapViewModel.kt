package com.bruno13palhano.sleeptight.ui.nap

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Nap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NapViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {

    val date = MutableStateFlow(0L)
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
        startTime.value = timeToMilliseconds(hour, minute)
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
        endTime.value = timeToMilliseconds(hour, minute)
    }

    val observation = MutableStateFlow("")

    private var _nap = MutableStateFlow(
        Nap(
            0L,
            0L,
            0L,
            0L,
            ""
        )
    )
    val nap = _nap.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = Nap(
                0L,
                0L,
                0L,
                0L,
                ""
            ),
            started = WhileSubscribed(5_000)
        )

    fun getNap(id: Long) {
        viewModelScope.launch {
            napRepository.getNapByIdStream(id).collect {
                _nap.value = it
                date.value = it.date
                startTime.value = it.startTime
                endTime.value = it.endTime
                observation.value = it.observation
            }
        }
    }

    fun updateNap(nap: Nap) {
        viewModelScope.launch {
            napRepository.updateNap(nap)
        }
    }

    private fun dateToMilliseconds(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        return calendar.timeInMillis
    }

    private fun timeToMilliseconds(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        return calendar.timeInMillis
    }
}