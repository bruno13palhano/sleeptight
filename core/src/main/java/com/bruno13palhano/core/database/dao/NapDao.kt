package com.bruno13palhano.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.database.model.NapEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: NapEntity): Long

    @Update
    suspend fun update(model: NapEntity)

    @Delete
    suspend fun delete(model: NapEntity)

    @Query("SELECT * FROM nap_table")
    fun getAll(): Flow<List<NapEntity>>

    @Query("SELECT * FROM nap_table WHERE id = :id")
    suspend fun getById(id: Long): NapEntity?

    @Query("DELETE FROM nap_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM nap_table WHERE id = (SELECT max(id) FROM nap_table)")
    fun getLast(): Flow<NapEntity>
}
