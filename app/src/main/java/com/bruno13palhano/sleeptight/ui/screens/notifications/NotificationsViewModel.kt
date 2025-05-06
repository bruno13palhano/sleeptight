package com.bruno13palhano.sleeptight.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.di.NotificationRep
import com.bruno13palhano.core.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    @NotificationRep private val notificationRepository: NotificationRepository,
) : ViewModel() {

    val allNotifications = notificationRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun deleteNotification(notificationId: Long, onNotificationDeleted: () -> Unit) {
        viewModelScope.launch {
            notificationRepository.deleteById(notificationId)
        }
        onNotificationDeleted()
    }
}
