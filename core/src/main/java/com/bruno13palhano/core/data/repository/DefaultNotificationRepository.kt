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
    override fun getAllStream(): Flow<List<Notification>> {
        return notificationDao.getAllNotificationsStream().map {
            it.map { notificationData ->
                notificationData.asNotification()
            }
        }
    }

    override fun getByIdStream(id: Long): Flow<Notification> {
        return notificationDao.getNotificationByIdStream(id).map {
                try {
                    it.asNotification()
                } catch (ignored: Exception) {
                    Notification(0L, "", "", 0L, 0L, false)
                }
            }
    }

    override suspend fun insert(model: Notification): Long {
        return notificationDao.insertNotification(model.asNotificationData())
    }

    override suspend fun deleteById(id: Long) {
        notificationDao.deleteNotificationById(id)
    }

    override suspend fun update(model: Notification) {
        notificationDao.updateNotification(model.asNotificationData())
    }

    override fun getLastStream(): Flow<Notification> {
        return notificationDao.getLastNotificationStream().map {
            try {
                it.asNotification()
            } catch (ignored: Exception) {
                Notification(
                    id = 0L,
                    title = "",
                    time = 0L,
                    date = 0L,
                    description = "",
                    repeat = false
                )
            }
        }
    }
}