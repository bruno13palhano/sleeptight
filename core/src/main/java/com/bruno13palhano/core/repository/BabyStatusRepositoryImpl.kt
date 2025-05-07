package com.bruno13palhano.core.repository

import com.bruno13palhano.core.database.dao.BabyStatusDao
import com.bruno13palhano.core.database.model.asBabyStatus
import com.bruno13palhano.core.database.model.asBabyStatusEntity
import com.bruno13palhano.model.BabyStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class BabyStatusRepositoryImpl @Inject constructor(
    private val babyStatusDao: BabyStatusDao,
) : BabyStatusRepository {
    override suspend fun insert(model: BabyStatus): Long {
        return babyStatusDao.insert(model.asBabyStatusEntity())
    }

    override fun getAll(): Flow<List<BabyStatus>> {
        return babyStatusDao.getAll()
            .map {
                it.map { babyStatusEntity -> babyStatusEntity.asBabyStatus() }
            }
    }

    override suspend fun getById(id: Long): BabyStatus? {
        return babyStatusDao.getById(id)?.asBabyStatus()
    }

    override suspend fun update(model: BabyStatus) {
        babyStatusDao.update(model.asBabyStatusEntity())
    }

    override suspend fun deleteById(id: Long) {
        babyStatusDao.deleteById(id)
    }

    override fun getLast(): Flow<BabyStatus> {
        return babyStatusDao.getLast()
            .map { it.asBabyStatus() }
            .catch {
                emit(
                    BabyStatus(
                        id = 0L,
                        title = "",
                        date = 0L,
                        height = 0F,
                        weight = 0F,
                    ),
                )
            }
    }
}
