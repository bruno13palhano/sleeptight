package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.BabyStatusData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BabyStatusDao : CommonDao<BabyStatusData>{

    @Query("SELECT * FROM baby_status_table")
    fun getAllStream(): Flow<List<BabyStatusData>>

    @Query("SELECT * FROM baby_status_table WHERE id = :id")
    fun getBabyStatusByIdStream(id: Long): Flow<BabyStatusData>

    @Query("DELETE FROM baby_status_table WHERE id = :id")
    suspend fun deleteBabyStatusById(id: Long)

    @Query("SELECT * FROM baby_status_table WHERE id = (SELECT max(id) FROM " +
            "baby_status_table)")
    fun getLastBabyStatusStream(): Flow<BabyStatusData>
}