package com.bruno13palhano.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.database.model.BabyStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BabyStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: BabyStatusEntity): Long

    @Update
    suspend fun update(model: BabyStatusEntity)

    @Delete
    suspend fun delete(model: BabyStatusEntity)

    @Query("SELECT * FROM baby_status_table")
    fun getAll(): Flow<List<BabyStatusEntity>>

    @Query("SELECT * FROM baby_status_table WHERE id = :id")
    fun getById(id: Long): Flow<BabyStatusEntity>

    @Query("DELETE FROM baby_status_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        "SELECT * FROM baby_status_table WHERE id = (SELECT max(id) FROM " +
            "baby_status_table)",
    )
    fun getLast(): Flow<BabyStatusEntity>
}
