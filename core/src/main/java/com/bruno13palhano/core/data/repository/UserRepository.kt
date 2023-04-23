package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User)
    fun getUserByIdStream(id: String): Flow<User>
    suspend fun updateUser(user: User)
}