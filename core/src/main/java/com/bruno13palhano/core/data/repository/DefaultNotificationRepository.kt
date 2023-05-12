package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.NotificationDao
import com.bruno13palhano.core.data.database.model.asNotification
import com.bruno13palhano.core.data.database.model.asNotificationData
import com.bruno13palhano.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultNotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override fun getAllNotificationsStream(): Flow<List<Notification>> {
        return notificationDao.getAllNotificationsStream().map {
            it.map { notificationData ->
                notificationData.asNotification()
            }
        }
    }

    override fun getNotificationByIdStream(id: Long): Flow<Notification> {
        return notificationDao.getNotificationByIdStream(id).map {
            it.asNotification()
        }
    }

    override suspend fun insertNotification(notification: Notification) {
        notificationDao.insertNotification(notification.asNotificationData())
    }

    override suspend fun deleteNotificationById(id: Long) {
        notificationDao.deleteNotificationById(id)
    }

    override suspend fun updateNotification(notification: Notification) {
        notificationDao.updateNotification(notification.asNotificationData())
    }
}