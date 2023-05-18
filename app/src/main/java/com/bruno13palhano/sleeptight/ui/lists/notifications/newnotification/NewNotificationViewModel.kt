package com.bruno13palhano.sleeptight.ui.lists.notifications.newnotification

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.core.data.repository.NotificationRepository
import com.bruno13palhano.model.Notification
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNotificationViewModel @Inject constructor(
    @DefaultNotificationRep private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val currentHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val repeat = MutableStateFlow(false)

    private val _hour = MutableStateFlow(currentHour.timeInMillis)
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

    private val _date = MutableStateFlow(MaterialDatePicker.todayInUtcMilliseconds())
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

    fun insertNotification() {
        val notification = Notification(
            id = 0L,
            title = title.value,
            description = description.value,
            time = _hour.value,
            date = _date.value,
            repeat = repeat.value
        )
        viewModelScope.launch {
            notificationRepository.insertNotification(notification)
        }
    }

    fun getLastNotification(): Flow<Notification> =
        notificationRepository.getLastNotificationStream()
}