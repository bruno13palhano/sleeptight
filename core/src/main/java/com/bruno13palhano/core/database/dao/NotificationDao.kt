package com.bruno13palhano.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.database.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: NotificationEntity): Long

    @Update
    suspend fun update(model: NotificationEntity)

    @Delete
    suspend fun delete(model: NotificationEntity)

    @Query("SELECT * FROM notification_table")
    fun getAll(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notification_table WHERE id = :id")
    fun getById(id: Long): Flow<NotificationEntity>

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        "SElECT * FROM notification_table WHERE id = (SELECT max(id) FROM " +
            "notification_table)",
    )
    fun getLast(): Flow<NotificationEntity>
}
