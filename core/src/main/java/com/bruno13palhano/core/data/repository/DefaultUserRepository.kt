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
        userDao.update(user.asUserData())
    }

    override suspend fun updateUserBabyName(babyName: String, id: String) {
        userDao.updateUserBabyName(babyName, id)
    }

    override suspend fun updateUserUrlPhoto(urlPhoto: String, id: String) {
        userDao.updateUserUrlPhoto(urlPhoto, id)
    }

    override suspend fun updateUserBirthplace(birthplace: String, id: String) {
        userDao.updateUserBirthplace(birthplace, id)
    }

    override suspend fun updateUserBirthdate(birthdate: Long, id: String) {
        userDao.updateUserBirthdate(birthdate, id)
    }

    override suspend fun updateUserBirthtime(birthtime: Long, id: String) {
        userDao.updateUserBirthtime(birthtime, id)
    }

    override suspend fun updateUserHeight(height: Float, id: String) {
        userDao.updateUserHeight(height, id)
    }

    override suspend fun updateUserWeight(weight: Float, id: String) {
        userDao.updateUserWeight(weight, id)
    }
}