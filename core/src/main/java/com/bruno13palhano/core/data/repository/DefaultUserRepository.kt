package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.database.dao.UserDao
import com.bruno13palhano.core.data.database.model.asUser
import com.bruno13palhano.core.data.database.model.asUserEntity
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
) : UserDataContract<User> {
    override suspend fun insert(user: User) {
        externalScope.launch {
            userDao.insert(user.asUserEntity())
        }
    }

    override fun getById(id: String): Flow<User> {
        return userDao.getById(id)
            .map { it.asUser() }
            .catch { it.printStackTrace() }
    }

    override suspend fun update(user: User) {
        externalScope.launch {
            userDao.update(user.asUserEntity())
        }
    }

    override suspend fun delete(user: User) {
        externalScope.launch {
            userDao.delete(user.asUserEntity())
        }
    }

    override suspend fun updateBabyName(babyName: String, id: String) {
        externalScope.launch {
            userDao.updateBabyName(babyName, id)
        }
    }

    override suspend fun updateUrlPhoto(urlPhoto: String, id: String) {
        externalScope.launch {
            userDao.updateUrlPhoto(urlPhoto, id)
        }
    }

    override suspend fun updateBirthplace(birthplace: String, id: String) {
        externalScope.launch {
            userDao.updateBirthplace(birthplace, id)
        }
    }

    override suspend fun updateBirthdate(birthdate: Long, id: String) {
        externalScope.launch {
            userDao.updateBirthdate(birthdate, id)
        }
    }

    override suspend fun updateBirthtime(birthtime: Long, id: String) {
        externalScope.launch {
            userDao.updateBirthtime(birthtime, id)
        }
    }

    override suspend fun updateHeight(height: Float, id: String) {
        externalScope.launch {
            userDao.updateHeight(height, id)
        }
    }

    override suspend fun updateWeight(weight: Float, id: String) {
        externalScope.launch {
            userDao.updateWeight(weight, id)
        }
    }
}