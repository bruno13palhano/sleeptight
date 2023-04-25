package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User)
    fun getUserByIdStream(id: String): Flow<User>
    suspend fun updateUser(user: User)
    suspend fun updateUserBabyName(babyName: String, id: String)
    suspend fun updateUserUrlPhoto(urlPhoto: String, id: String)
    suspend fun updateUserBirthplace(birthplace: String, id: String)
    suspend fun updateUserBirthdate(birthdate: Long, id: String)
    suspend fun updateUserBirthtime(birthtime: Long, id: String)
    suspend fun updateUserHeight(height: Float, id: String)
    suspend fun updateUserWeight(weight: Float, id: String)
}