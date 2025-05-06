package com.bruno13palhano.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getById(id: String): Flow<UserEntity>

    @Query("UPDATE user_table SET baby_name = :babyName WHERE id = :id")
    suspend fun updateBabyName(babyName: String, id: String)

    @Query("UPDATE user_table SET baby_url_photo = :urlPhoto WHERE id = :id")
    suspend fun updateUrlPhoto(urlPhoto: String, id: String)

    @Query("UPDATE user_table SET birth_place = :birthplace WHERE id = :id")
    suspend fun updateBirthplace(birthplace: String, id: String)

    @Query("UPDATE user_table SET birth_date = :birthdate WHERE id = :id")
    suspend fun updateBirthdate(birthdate: Long, id: String)

    @Query("UPDATE user_table SET birth_time = :birthtime WHERE id = :id")
    suspend fun updateBirthtime(birthtime: Long, id: String)

    @Query("UPDATE user_table SET height = :height WHERE id = :id")
    suspend fun updateHeight(height: Float, id: String)

    @Query("Update user_table SET weight = :weight WHERE id = :id")
    suspend fun updateWeight(weight: Float, id: String)
}
