package com.bruno13palhano.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NapDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(nap: NapData)

    @Query("SELECT * FROM nap_table")
    fun getAllStream(): Flow<List<NapData>>
}