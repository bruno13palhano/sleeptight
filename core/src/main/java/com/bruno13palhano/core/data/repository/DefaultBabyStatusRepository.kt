package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.BabyStatusDao
import com.bruno13palhano.core.data.database.model.asBabyStatus
import com.bruno13palhano.core.data.database.model.asBabyStatusData
import com.bruno13palhano.model.BabyStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultBabyStatusRepository @Inject constructor(
    private val babyStatusDao: BabyStatusDao
) : BabyStatusRepository {
    override suspend fun insertBabyStatus(babyStatus: BabyStatus) {
        babyStatusDao.insert(babyStatus.asBabyStatusData())
    }

    override fun getAllStream(): Flow<List<BabyStatus>> {
        return babyStatusDao.getAllStream().map {
            it.map { babyStatusData ->
                babyStatusData.asBabyStatus()
            }
        }
    }

    override fun getBabyStatusByIdStream(id: Long): Flow<BabyStatus> {
        return babyStatusDao.getBabyStatusByIdStream(id).map {
            it.asBabyStatus()
        }
    }

    override suspend fun updateBabyStatus(babyStatus: BabyStatus) {
        babyStatusDao.updateBabyStatus(babyStatus.asBabyStatusData())
    }

    override suspend fun deleteBabyStatusById(id: Long) {
        babyStatusDao.deleteBabyStatusById(id)
    }
}