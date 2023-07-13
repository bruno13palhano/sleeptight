package com.bruno13palhano.sleeptight.ui.screens.naps

import android.icu.text.DateFormat
import android.icu.util.Calendar
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
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNapViewModel @Inject constructor(
    @NapRep private val napRepository: CommonDataContract<Nap>
) : ViewModel() {
    var title by mutableStateOf("")
        private set
    var observations by mutableStateOf("")
        private set

    var dateInMillis by mutableLongStateOf(MaterialDatePicker.todayInUtcMilliseconds())
        private set
    var date by mutableStateOf(DateFormatUtil.format(dateInMillis))
        private set

    private var startTimeInMillis by mutableLongStateOf(Calendar.getInstance().timeInMillis)
    var startTime: String by mutableStateOf(DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTimeInMillis))
        private set
    var startTimeHour by mutableIntStateOf(getHour(startTimeInMillis))
        private set
    var startTimeMinute by mutableIntStateOf(getMinute(startTimeInMillis))
        private set

    private var endTimeInMillis by mutableLongStateOf(Calendar.getInstance().timeInMillis)
    var endTime: String by mutableStateOf(DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTimeInMillis))
        private set
    var endTimeHour by mutableIntStateOf(getHour(endTimeInMillis))
        private set
    var endTimeMinute by mutableIntStateOf(getMinute(endTimeInMillis))
        private set

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateObservations(observations: String) {
        this.observations = observations
    }

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = DateFormatUtil.format(date)
    }

    fun updateStartTime(hour: Int, minute: Int) {
        startTimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        this.startTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(startTimeInMillis)
    }

    fun updateEndTime(hour: Int, minute: Int) {
        endTimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        this.endTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(endTimeInMillis)
    }

    fun insertNap() {
        val nap = Nap(
            id = 0L,
            title = title,
            date = dateInMillis,
            startTime = startTimeInMillis,
            endTime = endTimeInMillis,
            sleepingTime = CalendarUtil.getSleepTime(startTimeInMillis, endTimeInMillis),
            observation = observations
        )

        viewModelScope.launch {
            napRepository.insert(nap)
        }
    }
}