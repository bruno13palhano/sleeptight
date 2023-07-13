package com.bruno13palhano.sleeptight.ui.screens.notifications

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
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.model.Notification
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.getHour
import com.bruno13palhano.sleeptight.ui.util.getMinute
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNotificationViewModel @Inject constructor(
    @DefaultNotificationRep private val notificationRepository: CommonDataContract<Notification>
) : ViewModel() {
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var repeat by mutableStateOf(false)
        private set
    var timeInMillis by mutableLongStateOf(Calendar.getInstance().timeInMillis)
        private set
    var timeHour by mutableIntStateOf(getHour(timeInMillis))
        private set
    var timeMinute by mutableIntStateOf(getMinute(timeInMillis))
        private set
    var time: String by mutableStateOf(DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE)
        .format(timeInMillis))
        private set
    var dateInMillis by mutableLongStateOf(MaterialDatePicker.todayInUtcMilliseconds())
        private set
    var date by mutableStateOf(DateFormatUtil.format(dateInMillis))
        private set

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updateRepeat(repeat: Boolean) {
        this.repeat = repeat
    }

    fun updateTime(hour: Int, minute: Int) {
        timeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        timeHour = getHour(timeInMillis)
        timeMinute = getMinute(timeInMillis)
        time = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(timeInMillis)
    }

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = DateFormatUtil.format(dateInMillis)
    }

    fun insertNotification(onNotificationInserted: (id: Long) -> Unit) {
        val notification = Notification(
            id = 0L,
            title = title,
            description = description,
            time = timeInMillis,
            date = dateInMillis,
            repeat = repeat
        )
        viewModelScope.launch {
            val id = notificationRepository.insert(notification)
            onNotificationInserted(id)
        }
    }
}