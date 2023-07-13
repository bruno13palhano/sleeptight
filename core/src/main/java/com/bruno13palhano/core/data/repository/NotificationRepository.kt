package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.database.dao.NotificationDao
import com.bruno13palhano.core.data.database.model.asNotification
import com.bruno13palhano.core.data.database.model.asNotificationEntity
import com.bruno13palhano.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) : CommonDataContract<Notification> {

    override fun getAll(): Flow<List<Notification>> {
        return notificationDao.getAll()
            .map {
                it.map { notificationEntity -> notificationEntity.asNotification() }
            }
    }

    override fun getById(id: Long): Flow<Notification> {
        return notificationDao.getById(id)
            .map { it.asNotification() }
            .catch { it.printStackTrace() }
    }

    override suspend fun insert(model: Notification): Long {
        return notificationDao.insert(model.asNotificationEntity())
    }

    override suspend fun deleteById(id: Long) {
        notificationDao.deleteById(id)
    }

    override suspend fun update(model: Notification) {
        notificationDao.update(model.asNotificationEntity())
    }

    override suspend fun delete(model: Notification) {
        notificationDao.delete(model.asNotificationEntity())
    }

    override fun getLast(): Flow<Notification> {
        return notificationDao.getLast()
            .map { it.asNotification() }
            .catch { it.printStackTrace() }
    }
}