package com.bruno13palhano.core.repository

import com.bruno13palhano.core.database.dao.NapDao
import com.bruno13palhano.core.database.model.asNap
import com.bruno13palhano.core.database.model.asNapEntity
import com.bruno13palhano.model.Nap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
internal class NapRepositoryImpl @Inject constructor(
    private val napDao: NapDao,
) : NapRepository {
    override suspend fun insert(model: Nap): Long {
        return napDao.insert(model.asNapEntity())
    }

    override fun getAll(): Flow<List<Nap>> {
        return napDao.getAll()
            .map {
                it.map { napEntity -> napEntity.asNap() }
            }
    }

    override suspend fun getById(id: Long): Nap? {
        return napDao.getById(id = id)?.asNap()
    }

    override suspend fun deleteById(id: Long) {
        napDao.deleteById(id)
    }

    override suspend fun update(model: Nap) {
        napDao.update(model.asNapEntity())
    }

    override fun getLast() = napDao.getLast()
        .map { it.asNap() }
        .catch {
            emit(
                Nap(
                    id = 0L,
                    title = "",
                    date = 0L,
                    startTime = 0L,
                    endTime = 0L,
                    sleepingTime = 0L,
                    observation = "",
                ),
            )
        }
}
