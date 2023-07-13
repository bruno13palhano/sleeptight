package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.database.dao.BabyStatusDao
import com.bruno13palhano.core.data.database.model.asBabyStatus
import com.bruno13palhano.core.data.database.model.asBabyStatusEntity
import com.bruno13palhano.model.BabyStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class BabyStatusRepository @Inject constructor(
    private val babyStatusDao: BabyStatusDao
) : CommonDataContract<BabyStatus> {
    override suspend fun insert(model: BabyStatus): Long {
        return babyStatusDao.insert(model.asBabyStatusEntity())
    }

    override fun getAll(): Flow<List<BabyStatus>> {
        return babyStatusDao.getAll()
            .map {
                it.map { babyStatusEntity -> babyStatusEntity.asBabyStatus() }
            }
    }

    override fun getById(id: Long): Flow<BabyStatus> {
        return babyStatusDao.getById(id)
            .map { it.asBabyStatus() }
            .catch { it.printStackTrace() }
    }

    override suspend fun update(model: BabyStatus) {
        babyStatusDao.update(model.asBabyStatusEntity())
    }

    override suspend fun delete(model: BabyStatus) {
        babyStatusDao.delete(model.asBabyStatusEntity())
    }

    override suspend fun deleteById(id: Long) {
        babyStatusDao.deleteById(id)
    }

    override fun getLast(): Flow<BabyStatus> {
        return babyStatusDao.getLast()
            .map { it.asBabyStatus() }
            .catch { it.printStackTrace() }
    }
}