package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.database.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(user: UserData)

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserByIdStream(id: String): Flow<UserData>

    @Update
    suspend fun updateUser(user: UserData)
}