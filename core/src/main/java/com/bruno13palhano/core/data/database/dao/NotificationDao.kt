package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.database.model.NotificationData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NotificationDao {

    @Query("SELECT * FROM notification_table")
    fun getAllNotificationsStream(): Flow<List<NotificationData>>

    @Query("SELECT * FROM notification_table WHERE id = :id")
    fun getNotificationByIdStream(id: Long): Flow<NotificationData>

    @Insert
    suspend fun insertNotification(notification: NotificationData)

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)

    @Update
    suspend fun updateNotification(notification: NotificationData)

    @Query("SElECT * FROM notification_table WHERE id = (SELECT max(id) FROM " +
            "notification_table)")
    fun getLastNotificationStream(): Flow<NotificationData>
}