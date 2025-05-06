package com.bruno13palhano.core.repository

import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insert(user: User)
    suspend fun update(user: User)
    suspend fun delete(user: User)
    fun getById(id: String): Flow<User>
    suspend fun updateBabyName(babyName: String, id: String)
    suspend fun updateUrlPhoto(urlPhoto: String, id: String)
    suspend fun updateBirthplace(birthplace: String, id: String)
    suspend fun updateBirthdate(birthdate: Long, id: String)
    suspend fun updateBirthtime(birthtime: Long, id: String)
    suspend fun updateHeight(height: Float, id: String)
    suspend fun updateWeight(weight: Float, id: String)
}
