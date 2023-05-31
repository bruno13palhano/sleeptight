package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.model.asNap
import com.bruno13palhano.core.data.database.model.asNapData
import com.bruno13palhano.model.Nap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultNapRepository @Inject constructor(
    private val napDao: NapDao
) : NapRepository {
    override suspend fun insert(model: Nap): Long {
        return napDao.insert(model.asNapData())
    }

    override fun getAllStream(): Flow<List<Nap>> {
        return napDao.getAllStream().map {
            it.map { nap ->
                nap.asNap()
            }
        }
    }

    override fun getByIdStream(id: Long): Flow<Nap> {
        return napDao.getNapByIdStream(id).map {
            try {
                it.asNap()
            } catch (ignored: Exception) {
                Nap(
                    id = 0L,
                    title = "",
                    date = 0L,
                    startTime = 0L,
                    endTime = 0L,
                    sleepingTime = 0L,
                    observation = ""
                )
            }
        }
    }

    override suspend fun update(model: Nap) {
        napDao.updateNap(model.asNapData())
    }

    override suspend fun deleteById(id: Long) {
        napDao.deleteNapById(id)
    }

    override fun getLastStream(): Flow<Nap> {
        return napDao.getLastNapStream().map {
            try {
                it.asNap()
            } catch (ignored: Exception) {
                Nap(
                    id = 0L,
                    title = "",
                    date = 0L,
                    startTime = 0L,
                    endTime = 0L,
                    sleepingTime = 0L,
                    observation = ""
                )
            }
        }
    }
}