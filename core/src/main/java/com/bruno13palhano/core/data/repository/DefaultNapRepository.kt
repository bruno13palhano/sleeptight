package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.asNap
import com.bruno13palhano.core.data.database.asNapData
import com.bruno13palhano.model.Nap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultNapRepository @Inject constructor(
    private val napDao: NapDao
) : NapRepository {
    override suspend fun insert(nap: Nap) {
        napDao.insert(nap.asNapData())
    }

    override fun getAllStream(): Flow<List<Nap>> {
        return napDao.getAllStream().map {
            it.map { nap ->
                nap.asNap()
            }
        }
    }

    override fun getNapByIdStream(id: Long): Flow<Nap> {
        return napDao.getNapByIdStream(id).map {
            try {
                it.asNap()
            } catch (ignored: Exception) {
                Nap(
                    id = 0L,
                    date = 0L,
                    startTime = 0L,
                    endTime = 0L,
                    sleepTime = 0L,
                    observation = ""
                )
            }
        }
    }

    override suspend fun updateNap(nap: Nap) {
        napDao.updateNap(nap.asNapData())
    }

    override suspend fun deleteNapById(id: Long) {
        napDao.deleteNapById(id)
    }
}