package com.bruno13palhano.sleeptight.ui.lists.nap

import android.icu.text.DateFormat
import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val isTitleNotEmpty = title.asStateFlow()
        .map { it.trim() != "" }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    fun setTitle(title: String) {
        this.title.value = title
    }

    var napTitle by mutableStateOf("")
        private set

    fun updateNapTitle(title: String) {
        napTitle = title
    }

    var dateInMillis by mutableLongStateOf(0L)
        private set
    var napDate by mutableStateOf("")
        private set

    fun updateNapDate(date: Long) {
        dateInMillis = date
        napDate = DateFormatUtil.format(date)
    }

    var startTimeInMillis by mutableLongStateOf(0L)
    var napStartTime by mutableStateOf("")
        private set

    var napStartTimeHour by mutableIntStateOf(0)
        private set
    var napStartTimeMinute by mutableIntStateOf(0)
        private set

    fun updateStartTime(hour: Int, minute: Int) {
        startTimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        napStartTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTimeInMillis)
    }

    fun updateNapStartTime(startTime: Long) {
        startTimeInMillis = startTime
        napStartTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTime)
    }

    var endTimeInMillis by mutableLongStateOf(0L)
    var napEndTime by mutableStateOf("")
        private set

    var napEndTimeHour by mutableIntStateOf(0)
        private set

    var napEndTimeMinute by mutableIntStateOf(0)
        private set

    fun updateEndTime(hour: Int, minute: Int) {
        endTimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        napEndTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTimeInMillis)
    }

    fun updateNapEndTime(endTime: Long) {
        endTimeInMillis = endTime
        napEndTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTime)
    }

    var napObservation by mutableStateOf("")
        private set

    fun updateNapObservation(observation: String) {
        napObservation = observation
    }

    val observation = MutableStateFlow("")

    fun getNap(id: Long) {
        viewModelScope.launch {
            napRepository.getByIdStream(id).collect {
                updateNapTitle(it.title)
                updateNapDate(it.date)
                updateNapStartTime(it.startTime)
                updateNapEndTime(it.endTime)
                updateNapObservation(it.observation)
                napStartTimeHour = getHour(it.startTime)
                napStartTimeMinute = getMinute(it.startTime)
                napEndTimeHour = getHour(it.endTime)
                napEndTimeMinute = getMinute(it.endTime)
                title.value = it.title
                date.value = it.date
                startTime.value = it.startTime
                endTime.value = it.endTime
                observation.value = it.observation
            }
        }
    }

    val uiState = combine(title, date, startTime, endTime, observation) {
            title, date, startTime, endTime ,observation ->
        NapUiState(
            title = title,
            date = DateFormatUtil.format(date),
            startTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTime),
            endTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTime),
            observation = observation
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = NapUiState()
        )

    data class NapUiState(
        val title: String = "",
        val date: String = "",
        val startTime: String = "",
        val endTime: String = "",
        val observation: String = ""
    )

    fun updateNap(id: Long) {
        val nap = Nap(
            id = id,
            title = napTitle,
            date = dateInMillis,
            startTime = startTimeInMillis,
            endTime = endTimeInMillis,
            sleepingTime = CalendarUtil.getSleepTime(startTimeInMillis, endTimeInMillis),
            observation = napObservation
        )
        napRepository.update(nap)
    }

    fun deleteNapById(id: Long) {
        napRepository.deleteById(id)
    }

    private fun getHour(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return calendar[Calendar.HOUR_OF_DAY]
    }

    private fun getMinute(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return calendar[Calendar.MINUTE]
    }
}