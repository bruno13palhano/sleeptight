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
    override suspend fun insert(model: BabyStatus): Long {
        return babyStatusDao.insert(model.asBabyStatusData())
    }

    override fun getAllStream(): Flow<List<BabyStatus>> {
        return babyStatusDao.getAllStream().map {
            it.map { babyStatusData ->
                babyStatusData.asBabyStatus()
            }
        }
    }

    override fun getByIdStream(id: Long): Flow<BabyStatus> {
        return babyStatusDao.getBabyStatusByIdStream(id).map {
            try {
                it.asBabyStatus()
            } catch (ignored: Exception) {
                BabyStatus(0L, "", 0L, 0F, 0F)
            }
        }
    }

    override suspend fun update(model: BabyStatus) {
        babyStatusDao.updateBabyStatus(model.asBabyStatusData())
    }

    override suspend fun deleteById(id: Long) {
        babyStatusDao.deleteBabyStatusById(id)
    }

    override fun getLastStream(): Flow<BabyStatus> {
        return babyStatusDao.getLastBabyStatusStream().map {
            try {
                it.asBabyStatus()
            } catch (ignored: Exception) {
                BabyStatus(
                    id = 0L,
                    title = "",
                    date = 0L,
                    height = 0F,
                    weight = 0F
                )
            }
        }
    }
}