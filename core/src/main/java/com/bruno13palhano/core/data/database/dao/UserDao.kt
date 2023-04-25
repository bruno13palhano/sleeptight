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

    @Query("UPDATE user_table SET baby_name = :babyName WHERE id = :id")
    suspend fun updateUserBabyName(babyName: String, id: String)

    @Query("UPDATE user_table SET baby_url_photo = :urlPhoto WHERE id = :id")
    suspend fun updateUserUrlPhoto(urlPhoto: String, id: String)

    @Query("UPDATE user_table SET birth_place = :birthplace WHERE id = :id")
    suspend fun updateUserBirthplace(birthplace: String, id: String)

    @Query("UPDATE user_table SET birth_date = :birthdate WHERE id = :id")
    suspend fun updateUserBirthdate(birthdate: Long, id: String)

    @Query("UPDATE user_table SET birth_time = :birthtime WHERE id = :id")
    suspend fun updateUserBirthtime(birthtime: Long, id: String)

    @Query("UPDATE user_table SET height = :height WHERE id = :id")
    suspend fun updateUserHeight(height: Float, id: String)

    @Query("Update user_table SET weight = :weight WHERE id = :id")
    suspend fun updateUserWeight(weight: Float, id: String)
}