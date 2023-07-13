package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.NotificationDao
import com.bruno13palhano.core.data.database.model.asNotification
import com.bruno13palhano.core.data.database.model.asNotificationEntity
import com.bruno13palhano.core.data.di.ApplicationScope
import com.bruno13palhano.model.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultNotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
    @ApplicationScope private val externalScope: CoroutineScope
) : NotificationRepository {

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
        externalScope.launch {
            notificationDao.deleteById(id)
        }
    }

    override suspend fun update(model: Notification) {
        externalScope.launch {
            notificationDao.update(model.asNotificationEntity())
        }
    }

    override suspend fun delete(model: Notification) {
        externalScope.launch {
            notificationDao.delete(model.asNotificationEntity())
        }
    }

    override fun getLast(): Flow<Notification> {
        return notificationDao.getLast()
            .map { it.asNotification() }
            .catch { it.printStackTrace() }
            .stateIn(
                scope = externalScope,
                started = WhileSubscribed(5_000),
                initialValue = Notification(
                    id = 0L,
                    title = "",
                    description = "",
                    time = 0L,
                    date = 0L,
                    repeat = false
                )
            )
    }
}