package com.bruno13palhano.sleeptight.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    @DefaultNotificationRep private val notificationRepository: CommonDataContract<Notification>
) : ViewModel() {

    val allNotifications = notificationRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteNotification(notificationId: Long, onNotificationDeleted: () -> Unit) {
        viewModelScope.launch {
            notificationRepository.deleteById(notificationId)
        }
        onNotificationDeleted()
    }
}