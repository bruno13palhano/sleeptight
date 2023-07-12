package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.database.data.UserDataContract
import com.bruno13palhano.core.data.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * [UserEntity] Dao interface.
 *
 * A Data Access Object for the [UserEntity] model.
 * This interface is responsible for handling [UserEntity] access to the Room database.
 */
@Dao
internal interface UserDao: UserDataContract<UserEntity> {

    /**
     * Inserts a [UserEntity] into the database.
     * @param user the new [UserEntity].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(user: UserEntity)

    /**
     * Updates the [UserEntity] in the database.
     * @param user the [UserEntity] to be updated.
     */
    @Update
    override suspend fun update(user: UserEntity)

    /**
     * Deletes the [UserEntity] in the database.
     * @param user the [UserEntity] to be deleted.
     */
    @Delete
    override suspend fun delete(user: UserEntity)

    /**
     * Gets the [UserEntity] specified by this [id].
     * @param id the [id] for this [UserEntity].
     * @return a [Flow] of [UserEntity].
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    override fun getById(id: String): Flow<UserEntity>

    /**
     * Updates the [babyName] for the [UserEntity] specified by this [id].
     * @param babyName baby's new name.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET baby_name = :babyName WHERE id = :id")
    override suspend fun updateBabyName(babyName: String, id: String)

    /**
     * Updates the [urlPhoto] for the [UserEntity] specified by this [id].
     * @param urlPhoto the new [urlPhoto].
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET baby_url_photo = :urlPhoto WHERE id = :id")
    override suspend fun updateUrlPhoto(urlPhoto: String, id: String)

    /**
     * Updates baby's [birthplace] for the [UserEntity] specified by this [id].
     * @param birthplace baby's new place of birth.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET birth_place = :birthplace WHERE id = :id")
    override suspend fun updateBirthplace(birthplace: String, id: String)

    /**
     * Updates baby's [birthdate] for the [UserEntity] specified by this [id].
     * @param birthdate baby's new date of birth in milliseconds.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET birth_date = :birthdate WHERE id = :id")
    override suspend fun updateBirthdate(birthdate: Long, id: String)

    /**
     * Updates baby's [birthtime] for the [UserEntity] specified by this [id].
     * @param birthtime baby's new time of birth in milliseconds.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET birth_time = :birthtime WHERE id = :id")
    override suspend fun updateBirthtime(birthtime: Long, id: String)

    /**
     * Updates baby's [height] for the [UserEntity] specified by this [id].
     * @param height baby's new [height] at birth time.
     * @param id the [id] for this [UserEntity].
     */
    @Query("UPDATE user_table SET height = :height WHERE id = :id")
    override suspend fun updateHeight(height: Float, id: String)

    /**
     * Updates baby's [weight] for the [UserEntity] specified by this [id].
     * @param weight baby's new [weight] at birth time.
     * @param id the [id] for this [UserEntity].
     */
    @Query("Update user_table SET weight = :weight WHERE id = :id")
    override suspend fun updateWeight(weight: Float, id: String)
}