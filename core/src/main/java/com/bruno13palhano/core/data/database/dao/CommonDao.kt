package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

@Dao
interface CommonDao<T> {

    @Insert(onConflict = REPLACE)
    suspend fun insert(model: T): Long

    @Update
    suspend fun update(model: T)

    @Delete
    suspend fun delete(model: T)
}