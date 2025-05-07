package com.bruno13palhano.core.repository

import com.bruno13palhano.core.database.dao.NotificationDao
import com.bruno13palhano.core.database.model.asNotification
import com.bruno13palhano.core.database.model.asNotificationEntity
import com.bruno13palhano.model.Notification
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
internal class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao,
) : NotificationRepository {

    override fun getAll(): Flow<List<Notification>> {
        return notificationDao.getAll()
            .map {
                it.map { notificationEntity -> notificationEntity.asNotification() }
            }
    }

    override suspend fun getById(id: Long): Notification? {
        return notificationDao.getById(id)?.asNotification()
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

    override fun getLast(): Flow<Notification> {
        return notificationDao.getLast()
            .map { it.asNotification() }
            .catch {
                emit(
                    Notification(
                        id = 0L,
                        title = "",
                        description = "",
                        time = 0L,
                        date = 0L,
                        repeat = false,
                    ),
                )
            }
    }
}
