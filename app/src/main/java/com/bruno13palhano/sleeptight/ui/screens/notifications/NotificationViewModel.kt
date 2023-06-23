package com.bruno13palhano.sleeptight.ui.screens.notifications

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.core.data.repository.NotificationRepository
import com.bruno13palhano.model.Notification
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @DefaultNotificationRep private val notificationRepository: NotificationRepository
) : ViewModel() {

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val repeat = MutableStateFlow(false)

    val isTitleNotEmpty = title.asStateFlow()
        .map { it.trim() != "" }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    private val _hour = MutableStateFlow(0L)
    val hour = _hour.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = 0L,
            started = WhileSubscribed(5_000)
        )

    val hourUi = _hour.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setHour(hour: Int, minute: Int) {
        _hour.value = CalendarUtil.timeToMilliseconds(hour, minute)
    }

    private val _date = MutableStateFlow(0L)
    val date = _date.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = 0L,
            started = WhileSubscribed(5_000)
        )

    val dateUi = _date.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(date: Long) {
        _date.value = date
    }

    fun setNotification(id: Long) {
        viewModelScope.launch {
            notificationRepository.getByIdStream(id).collect {
                title.value = it.title
                description.value = it.description
                repeat.value = it.repeat
                _hour.value = it.time
                _date.value = it.date
            }
        }
    }

    fun updateNotification(id: Long) {
        val notification = Notification(
            id = id,
            title = title.value,
            description = description.value,
            repeat = repeat.value,
            time = _hour.value,
            date = _date.value
        )
        viewModelScope.launch {
            notificationRepository.update(notification)
        }
    }

    fun deleteNotification(id: Long, onNotificationDeleted: () -> Unit) {
        viewModelScope.launch {
            notificationRepository.deleteById(id)
            onNotificationDeleted()
        }
    }
}