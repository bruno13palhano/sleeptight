package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.database.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * [NotificationEntity] Dao interface.
 *
 * A Data Access Object for the [NotificationEntity] model.
 * This interface is responsible for handling [NotificationEntity] access to the Room database.
 */
@Dao
internal interface NotificationDao : CommonDataContract<NotificationEntity> {

    /**
     * Inserts a [NotificationEntity] into the database.
     * @param model the new [NotificationEntity].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: NotificationEntity): Long

    /**
     * Updates the [NotificationEntity] in the database.
     * @param model the [NotificationEntity] to be updated.
     */
    @Update
    override suspend fun update(model: NotificationEntity)

    /**
     * Deletes the [NotificationEntity] in the database.
     * @param model the [NotificationEntity] to be deleted.
     */
    @Delete
    override suspend fun delete(model: NotificationEntity)

    /**
     * Gets all [NotificationEntity].
     * @return a [Flow] containing a [List] of all [NotificationEntity].
     */
    @Query("SELECT * FROM notification_table")
    override fun getAll(): Flow<List<NotificationEntity>>

    /**
     * Gets the [NotificationEntity] specified by this [id].
     * @param id the [id] for this [NotificationEntity].
     * @return a [Flow] of [NotificationEntity].
     */
    @Query("SELECT * FROM notification_table WHERE id = :id")
    override fun getById(id: Long): Flow<NotificationEntity>

    /**
     * Deletes the [NotificationEntity] specified by this [id].
     * @param id the [id] for this [NotificationEntity].
     */
    @Query("DELETE FROM notification_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    /**
     * Gets the last [NotificationEntity] inserted into the database.
     * @return a [Flow] of [NotificationEntity].
     */
    @Query("SElECT * FROM notification_table WHERE id = (SELECT max(id) FROM " +
            "notification_table)")
    override fun getLast(): Flow<NotificationEntity>
}