package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.UserDao
import com.bruno13palhano.core.data.database.model.asUser
import com.bruno13palhano.core.data.database.model.asUserData
import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultUserRepository @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insert(user.asUserData())
    }

    override fun getUserByIdStream(id: String): Flow<User> {
        return userDao.getUserByIdStream(id).map {
            it.asUser()
        }
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.asUserData())
    }
}