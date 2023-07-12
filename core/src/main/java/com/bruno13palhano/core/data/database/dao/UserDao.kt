package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * [UserEntity] Dao interface.
 *
 * A Data Access Object for the [UserEntity] model.
 * This interface is responsible for handling [UserEntity] access to the Room database.
 */
@Dao
internal interface UserDao : CommonDao<UserEntity>{

    /**
     * Gets the [UserEntity] specified by this [id].
     * @param id the [id] for this [UserEntity].
     * @return a [Flow] of [UserEntity].
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserByIdStream(id: String): Flow<UserEntity>

    /**
     * Updates the [babyName] for the [UserEntity] specified by this [id].
     * @param babyName baby's new name.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET baby_name = :babyName WHERE id = :id")
    suspend fun updateUserBabyName(babyName: String, id: String)

    /**
     * Updates the [urlPhoto] for the [UserEntity] specified by this [id].
     * @param urlPhoto the new [urlPhoto].
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET baby_url_photo = :urlPhoto WHERE id = :id")
    suspend fun updateUserUrlPhoto(urlPhoto: String, id: String)

    /**
     * Updates baby's [birthplace] for the [UserEntity] specified by this [id].
     * @param birthplace baby's new place of birth.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET birth_place = :birthplace WHERE id = :id")
    suspend fun updateUserBirthplace(birthplace: String, id: String)

    /**
     * Updates baby's [birthdate] for the [UserEntity] specified by this [id].
     * @param birthdate baby's new date of birth in milliseconds.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET birth_date = :birthdate WHERE id = :id")
    suspend fun updateUserBirthdate(birthdate: Long, id: String)

    /**
     * Updates baby's [birthtime] for the [UserEntity] specified by this [id].
     * @param birthtime baby's new time of birth in milliseconds.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET birth_time = :birthtime WHERE id = :id")
    suspend fun updateUserBirthtime(birthtime: Long, id: String)

    /**
     * Updates baby's [height] for the [UserEntity] specified by this [id].
     * @param height baby's new [height] at birth time.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET height = :height WHERE id = :id")
    suspend fun updateUserHeight(height: Float, id: String)

    /**
     * Updates baby's [weight] for the [UserEntity] specified by this [id].
     * @param weight baby's new [weight] at birth time.
     * @param id the [id] for this [UserEntity].
     */
    @Query("Update user_table SET weight = :weight WHERE id = :id")
    suspend fun updateUserWeight(weight: Float, id: String)
}