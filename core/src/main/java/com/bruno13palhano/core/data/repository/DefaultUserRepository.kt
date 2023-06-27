package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.UserDao
import com.bruno13palhano.core.data.database.model.asUser
import com.bruno13palhano.core.data.database.model.asUserData
import com.bruno13palhano.core.data.di.ApplicationScope
import com.bruno13palhano.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultUserRepository @Inject constructor(
    private val userDao: UserDao,
    @ApplicationScope private val externalScope: CoroutineScope
) : UserRepository {
    override fun insertUser(user: User) {
        externalScope.launch {
            userDao.insert(user.asUserData())
        }
    }

    override fun getUserByIdStream(id: String): Flow<User> {
        return userDao.getUserByIdStream(id)
            .map { it.asUser() }
            .catch { it.printStackTrace() }
    }

    override fun updateUser(user: User) {
        externalScope.launch {
            userDao.update(user.asUserData())
        }
    }

    override fun updateUserBabyName(babyName: String, id: String) {
        externalScope.launch {
            userDao.updateUserBabyName(babyName, id)
        }
    }

    override fun updateUserUrlPhoto(urlPhoto: String, id: String) {
        externalScope.launch {
            userDao.updateUserUrlPhoto(urlPhoto, id)
        }
    }

    override fun updateUserBirthplace(birthplace: String, id: String) {
        externalScope.launch {
            userDao.updateUserBirthplace(birthplace, id)
        }
    }

    override fun updateUserBirthdate(birthdate: Long, id: String) {
        externalScope.launch {
            userDao.updateUserBirthdate(birthdate, id)
        }
    }

    override fun updateUserBirthtime(birthtime: Long, id: String) {
        externalScope.launch {
            userDao.updateUserBirthtime(birthtime, id)
        }
    }

    override fun updateUserHeight(height: Float, id: String) {
        externalScope.launch {
            userDao.updateUserHeight(height, id)
        }
    }

    override fun updateUserWeight(weight: Float, id: String) {
        externalScope.launch {
            userDao.updateUserWeight(weight, id)
        }
    }
}