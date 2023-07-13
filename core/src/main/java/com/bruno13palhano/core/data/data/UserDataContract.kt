package com.bruno13palhano.core.data.data

import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Contract for [User] data manipulation.
 *
 * An interface to decouple the treatment of [User] data, regardless of concrete implementation.
 * @param T the user model type.
 */
interface UserDataContract<T> {

    /**
     * Inserts a [user] of type T.
     * @param user the new [user] to be inserted.
     */
    suspend fun insert(user: T)

    /**
     * Updates the [user] of type [T].
     * @param user the [user] to be updated.
     */
    suspend fun update(user: T)

    /**
     * Deletes the [user] of type [T].
     * @param user the [user] to be deleted.
     */
    suspend fun delete(user: T)

    /**
     * Gets the user of type [T] specified by this [id].
     * @param id the [id] for this user of type [T].
     * @return a [Flow] containing a user of type [T].
     */
    fun getById(id: String): Flow<T>

    /**
     * Updates [babyName] for the user specified by this [id].
     * @param babyName baby's new name.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateBabyName(babyName: String, id: String)

    /**
     * Updates baby's [urlPhoto] for the user specified by this [id].
     * @param urlPhoto baby's new url photo.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateUrlPhoto(urlPhoto: String, id: String)

    /**
     * Updates baby's [birthplace] for the user specified by this [id].
     * @param birthplace baby's new place of birth.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateBirthplace(birthplace: String, id: String)

    /**
     * Updates baby's [birthdate] for the user specified by this [id].
     * @param birthdate baby's new date of birth.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateBirthdate(birthdate: Long, id: String)

    /**
     * Updates baby's [birthtime] for the user specified by this [id].
     * @param birthtime baby's new time of birth.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateBirthtime(birthtime: Long, id: String)

    /**
     * Updates baby's [height] for the user specified by this [id].
     * @param height baby's new height at birth.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateHeight(height: Float, id: String)

    /**
     * Updates baby's [weight] for the user specified by this [id].
     * @param weight baby's new weight at birth.
     * @param id the [id] for this user of type [T].
     */
    suspend fun updateWeight(weight: Float, id: String)
}