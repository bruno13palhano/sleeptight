package com.bruno13palhano.sleeptight.ui.newnap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.model.Nap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewNapViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {
    private val currentDate = Calendar.getInstance()
    private val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
    private val currentMonth = currentDate.get(Calendar.MONTH)
    private val currentYear = currentDate.get(Calendar.YEAR)
    private val currentMinute = currentDate.get(Calendar.MINUTE)
    private val currentHour = currentDate.get(Calendar.HOUR)

    val day = MutableStateFlow(currentDay)
    val month = MutableStateFlow(currentMonth)
    val year = MutableStateFlow(currentYear)
    val date = combine(day, month, year) { day, month, year ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        calendar.timeInMillis
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = 0L,
            started = WhileSubscribed(5_000)
        )

    val startMinute = MutableStateFlow(currentMinute)
    val startHour = MutableStateFlow(currentHour)
    val startTime = combine(startMinute, startHour) { minute, hour ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.HOUR, hour)

        calendar.timeInMillis
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = 0L,
            started = WhileSubscribed(5_000)
        )

    val endMinute = MutableStateFlow(currentMinute)
    val endHour = MutableStateFlow(currentHour)
    val endTime = combine(endMinute, endHour) { minute, hour ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.HOUR, hour)

        calendar.timeInMillis
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = 0L,
            started = WhileSubscribed(5_000)
        )

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
}