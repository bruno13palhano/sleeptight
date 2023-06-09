package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.NotificationDao
import com.bruno13palhano.core.data.database.model.asNotification
import com.bruno13palhano.core.data.database.model.asNotificationData
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

    override val all: Flow<List<Notification>> = notificationDao.getAllNotificationsStream()
        .map {
            it.map { notificationData -> notificationData.asNotification() }
        }
        .stateIn(
            scope = externalScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    override fun getByIdStream(id: Long): Flow<Notification> {
        return notificationDao.getNotificationByIdStream(id)
            .map { it.asNotification() }
            .catch { it.printStackTrace() }
    }

    override suspend fun insert(model: Notification): Long {
        return notificationDao.insert(model.asNotificationData())
    }

    override fun deleteById(id: Long) {
        externalScope.launch {
            notificationDao.deleteNotificationById(id)
        }
    }

    override fun update(model: Notification) {
        externalScope.launch {
            notificationDao.update(model.asNotificationData())
        }
    }

    override val last: Flow<Notification> = notificationDao.getLastNotificationStream()
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