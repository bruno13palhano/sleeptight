package com.bruno13palhano.core.repository

import com.bruno13palhano.core.database.dao.UserDao
import com.bruno13palhano.core.database.model.asUser
import com.bruno13palhano.core.database.model.asUserEntity
import com.bruno13palhano.model.User
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    override suspend fun insert(user: User) {
        userDao.insert(user.asUserEntity())
    }

    override fun getById(id: String): Flow<User> {
        return userDao.getById(id)
            .map { it.asUser() }
            .catch { it.printStackTrace() }
    }

    override suspend fun update(user: User) {
        userDao.update(user.asUserEntity())
    }

    override suspend fun delete(user: User) {
        userDao.delete(user.asUserEntity())
    }

    override suspend fun updateBabyName(babyName: String, id: String) {
        userDao.updateBabyName(babyName, id)
    }

    override suspend fun updateUrlPhoto(urlPhoto: String, id: String) {
        userDao.updateUrlPhoto(urlPhoto, id)
    }

    override suspend fun updateBirthplace(birthplace: String, id: String) {
        userDao.updateBirthplace(birthplace, id)
    }

    override suspend fun updateBirthdate(birthdate: Long, id: String) {
        userDao.updateBirthdate(birthdate, id)
    }

    override suspend fun updateBirthtime(birthtime: Long, id: String) {
        userDao.updateBirthtime(birthtime, id)
    }

    override suspend fun updateHeight(height: Float, id: String) {
        userDao.updateHeight(height, id)
    }

    override suspend fun updateWeight(weight: Float, id: String) {
        userDao.updateWeight(weight, id)
    }
}
