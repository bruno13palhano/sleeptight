package com.bruno13palhano.core.data.database.data

import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Contract for [User] data manipulation.
 *
 * An interface to decouple the treatment of [User] data, regardless of concrete implementation.
 */
interface UserDataContract<T> {
    suspend fun insert(user: T)
    suspend fun update(user: T)
    suspend fun delete(user: T)
    fun getById(id: String): Flow<T>
    suspend fun updateBabyName(babyName: String, id: String)
    suspend fun updateUrlPhoto(urlPhoto: String, id: String)
    suspend fun updateBirthplace(birthplace: String, id: String)
    suspend fun updateBirthdate(birthdate: Long, id: String)
    suspend fun updateBirthtime(birthtime: Long, id: String)
    suspend fun updateHeight(height: Float, id: String)
    suspend fun updateWeight(weight: Float, id: String)
}