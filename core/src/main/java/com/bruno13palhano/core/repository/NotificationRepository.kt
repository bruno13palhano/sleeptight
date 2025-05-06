package com.bruno13palhano.core.repository

import com.bruno13palhano.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun insert(notification: Notification): Long
    suspend fun update(notification: Notification)
    suspend fun deleteById(id: Long)
    fun getById(id: Long): Flow<Notification>
    fun getAll(): Flow<List<Notification>>
    fun getLast(): Flow<Notification>
}
