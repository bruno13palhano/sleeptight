package com.bruno13palhano.sleeptight.ui.lists.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.core.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    @DefaultNotificationRep private val notificationRepository: NotificationRepository
) : ViewModel() {

    val allNotifications = notificationRepository.getAllNotificationsStream()
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = WhileSubscribed(5_000)
        )

    fun deleteNotification(notificationId: Long) {
        viewModelScope.launch {
            notificationRepository.deleteNotificationById(notificationId)
        }
    }
}