package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotificationsStream(): Flow<List<Notification>>
    fun getNotificationByIdStream(id: Long): Flow<Notification>
    suspend fun insertNotification(notification: Notification)
    suspend fun deleteNotificationById(id: Long)
    suspend fun updateNotification(notification: Notification)
}