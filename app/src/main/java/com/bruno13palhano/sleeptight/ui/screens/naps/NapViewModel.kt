package com.bruno13palhano.sleeptight.ui.screens.naps

import android.icu.text.DateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.di.NapRep
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.getHour
import com.bruno13palhano.sleeptight.ui.util.getMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NapViewModel @Inject constructor(
    @NapRep private val napRepository: CommonDataContract<Nap>,
) : ViewModel() {

    var title by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var date by mutableStateOf("")
        private set
    var startTimeInMillis by mutableLongStateOf(0L)
        private set
    var startTime by mutableStateOf("")
        private set
    var startTimeHour by mutableIntStateOf(0)
        private set
    var startTimeMinute by mutableIntStateOf(0)
        private set
    var endTimeInMillis by mutableLongStateOf(0L)
        private set
    var endTime by mutableStateOf("")
        private set
    var endTimeHour by mutableIntStateOf(0)
        private set
    var endTimeMinute by mutableIntStateOf(0)
        private set
    var observation by mutableStateOf("")
        private set

    fun updateNapTitle(title: String) {
        this.title = title
    }

    fun updateNapDate(date: Long) {
        dateInMillis = date
        this.date = DateFormatUtil.format(date)
    }

    fun updateNapObservation(observation: String) {
        this.observation = observation
    }

    fun updateStartTime(hour: Int, minute: Int) {
        startTimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        startTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTimeInMillis)
    }

    fun updateEndTime(hour: Int, minute: Int) {
        endTimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        endTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTimeInMillis)
    }

    fun getNap(id: Long) {
        viewModelScope.launch {
            napRepository.getById(id).collect {
                updateNapTitle(it.title)
                updateNapDate(it.date)
                updateNapStartTime(it.startTime)
                updateNapEndTime(it.endTime)
                updateNapObservation(it.observation)
                startTimeHour = getHour(it.startTime)
                startTimeMinute = getMinute(it.startTime)
                endTimeHour = getHour(it.endTime)
                endTimeMinute = getMinute(it.endTime)
            }
        }
    }

    fun updateNap(id: Long) {
        val nap = Nap(
            id = id,
            title = title,
            date = dateInMillis,
            startTime = startTimeInMillis,
            endTime = endTimeInMillis,
            sleepingTime = CalendarUtil.getSleepTime(startTimeInMillis, endTimeInMillis),
            observation = observation,
        )
        viewModelScope.launch {
            napRepository.update(nap)
        }
    }

    fun deleteNapById(id: Long) {
        viewModelScope.launch {
            napRepository.deleteById(id)
        }
    }

    private fun updateNapStartTime(startTime: Long) {
        startTimeInMillis = startTime
        this.startTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTime)
    }

    private fun updateNapEndTime(endTime: Long) {
        endTimeInMillis = endTime
        this.endTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTime)
    }
}
