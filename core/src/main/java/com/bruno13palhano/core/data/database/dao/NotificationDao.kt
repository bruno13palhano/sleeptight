package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * [NotificationEntity] Dao interface.
 *
 * A Data Access Object for the [NotificationEntity] model.
 * This interface is responsible for handling [NotificationEntity] access to the Room database.
 */
@Dao
internal interface NotificationDao : CommonDao<NotificationEntity> {

    /**
     * Gets all [NotificationEntity].
     * @return a [Flow] containing a [List] of all [NotificationEntity].
     */
    @Query("SELECT * FROM notification_table")
    fun getAllNotificationsStream(): Flow<List<NotificationEntity>>

    /**
     * Gets the [NotificationEntity] specified by this [id].
     * @param id the [id] for this [NotificationEntity].
     * @return a [Flow] of [NotificationEntity].
     */
    @Query("SELECT * FROM notification_table WHERE id = :id")
    fun getNotificationByIdStream(id: Long): Flow<NotificationEntity>

    /**
     * Deletes the [NotificationEntity] specified by this [id].
     * @param id the [id] for this [NotificationEntity].
     */
    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)

    /**
     * Gets the last [NotificationEntity] inserted into the database.
     * @return a [Flow] of [NotificationEntity].
     */
    @Query("SElECT * FROM notification_table WHERE id = (SELECT max(id) FROM " +
            "notification_table)")
    fun getLastNotificationStream(): Flow<NotificationEntity>
}