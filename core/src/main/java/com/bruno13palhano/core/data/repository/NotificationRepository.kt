package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository : RepositoryCrud<Notification>{
    fun getLastNotificationStream(): Flow<Notification>
}