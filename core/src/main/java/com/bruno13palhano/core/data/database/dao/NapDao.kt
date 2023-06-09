package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.NapData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NapDao : CommonDao<NapData> {

    @Query("SELECT * FROM nap_table")
    fun getAllStream(): Flow<List<NapData>>

    @Query("SELECT * FROM nap_table WHERE id = :id")
    fun getNapByIdStream(id: Long): Flow<NapData>

    @Query("DELETE FROM nap_table WHERE id = :id")
    suspend fun deleteNapById(id: Long)

    @Query("SELECT * FROM nap_table WHERE id = (SELECT max(id) FROM nap_table)")
    fun getLastNapStream(): Flow<NapData>
}