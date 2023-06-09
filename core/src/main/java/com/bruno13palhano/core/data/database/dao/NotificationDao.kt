package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.NotificationData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NotificationDao : CommonDao<NotificationData> {

    @Query("SELECT * FROM notification_table")
    fun getAllNotificationsStream(): Flow<List<NotificationData>>

    @Query("SELECT * FROM notification_table WHERE id = :id")
    fun getNotificationByIdStream(id: Long): Flow<NotificationData>

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)

    @Query("SElECT * FROM notification_table WHERE id = (SELECT max(id) FROM " +
            "notification_table)")
    fun getLastNotificationStream(): Flow<NotificationData>
}