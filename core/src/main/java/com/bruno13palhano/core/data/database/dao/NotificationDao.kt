package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.NotificationData
import kotlinx.coroutines.flow.Flow

/**
 * [NotificationData] Dao interface.
 *
 * A Data Access Object for the [NotificationData] model.
 * This interface is responsible for handling [NotificationData] access to the Room database.
 */
@Dao
internal interface NotificationDao : CommonDao<NotificationData> {

    /**
     * Gets all [NotificationData].
     * @return a [Flow] containing a [List] of all [NotificationData].
     */
    @Query("SELECT * FROM notification_table")
    fun getAllNotificationsStream(): Flow<List<NotificationData>>

    /**
     * Gets the [NotificationData] specified by this [id].
     * @param id the [id] for this [NotificationData].
     * @return a [Flow] of [NotificationData].
     */
    @Query("SELECT * FROM notification_table WHERE id = :id")
    fun getNotificationByIdStream(id: Long): Flow<NotificationData>

    /**
     * Deletes the [NotificationData] specified by this [id].
     * @param id the [id] for this [NotificationData].
     */
    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)

    /**
     * Gets the last [NotificationData] inserted into the database.
     * @return a [Flow] of [NotificationData].
     */
    @Query("SElECT * FROM notification_table WHERE id = (SELECT max(id) FROM " +
            "notification_table)")
    fun getLastNotificationStream(): Flow<NotificationData>
}