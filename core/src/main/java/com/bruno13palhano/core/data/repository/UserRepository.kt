package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun insertUser(user: User)
    fun getUserByIdStream(id: String): Flow<User>
    fun updateUser(user: User)
    fun updateUserBabyName(babyName: String, id: String)
    fun updateUserUrlPhoto(urlPhoto: String, id: String)
    fun updateUserBirthplace(birthplace: String, id: String)
    fun updateUserBirthdate(birthdate: Long, id: String)
    fun updateUserBirthtime(birthtime: Long, id: String)
    fun updateUserHeight(height: Float, id: String)
    fun updateUserWeight(weight: Float, id: String)
}