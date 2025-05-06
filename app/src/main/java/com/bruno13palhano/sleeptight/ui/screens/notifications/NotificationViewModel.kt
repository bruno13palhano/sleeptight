package com.bruno13palhano.sleeptight.ui.screens.notifications

import android.icu.text.DateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.di.NotificationRep
import com.bruno13palhano.core.repository.NotificationRepository
import com.bruno13palhano.model.Notification
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.getHour
import com.bruno13palhano.sleeptight.ui.util.getMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @NotificationRep private val notificationRepository: NotificationRepository,
) : ViewModel() {

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var repeat by mutableStateOf(false)
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var date by mutableStateOf("")
        private set
    var timeInMillis by mutableLongStateOf(0L)
        private set
    var timeHour by mutableIntStateOf(0)
        private set
    var timeMinute by mutableIntStateOf(0)
        private set
    var time: String by mutableStateOf("")
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

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = DateFormatUtil.format(dateInMillis)
    }

    fun updateTime(hour: Int, minute: Int) {
        timeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        timeHour = getHour(timeInMillis)
        timeMinute = getMinute(timeInMillis)
        this.time = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(timeInMillis)
    }

    fun setNotification(id: Long) {
        viewModelScope.launch {
            notificationRepository.getById(id).collect {
                title = it.title
                description = it.description
                repeat = it.repeat
                timeInMillis = it.time
                timeHour = getHour(timeInMillis)
                timeMinute = getMinute(timeInMillis)
                time = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(timeInMillis)
                dateInMillis = it.date
                date = DateFormatUtil.format(dateInMillis)
            }
        }
    }

    fun updateNotification(id: Long) {
        val notification = Notification(
            id = id,
            title = title,
            description = description,
            repeat = repeat,
            time = timeInMillis,
            date = dateInMillis,
        )
        viewModelScope.launch {
            notificationRepository.update(notification)
        }
    }

    fun deleteNotification(id: Long, onNotificationDeleted: () -> Unit) {
        viewModelScope.launch {
            notificationRepository.deleteById(id)
        }
        onNotificationDeleted()
    }
}
