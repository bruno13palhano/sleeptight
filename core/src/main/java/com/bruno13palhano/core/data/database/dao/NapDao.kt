package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.database.model.NapData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NapDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(nap: NapData)

    @Query("SELECT * FROM nap_table")
    fun getAllStream(): Flow<List<NapData>>

    @Query("SELECT * FROM nap_table WHERE id = :id")
    fun getNapByIdStream(id: Long): Flow<NapData>

    @Update
    suspend fun updateNap(nap: NapData)

    @Query("DELETE FROM nap_table WHERE id = :id")
    suspend fun deleteNapById(id: Long)
}