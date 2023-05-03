package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.database.model.BabyStatusData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BabyStatusDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(babyStatus: BabyStatusData)

    @Query("SELECT * FROM baby_status_table")
    fun getAllStream(): Flow<List<BabyStatusData>>

    @Query("SELECT * FROM baby_status_table WHERE id = :id")
    fun getBabyStatusByIdStream(id: Long): Flow<BabyStatusData>

    @Update
    suspend fun updateBabyStatus(babyStatus: BabyStatusData)

    @Query("DELETE FROM baby_status_table WHERE id = :id")
    suspend fun deleteBabyStatusById(id: Long)
}