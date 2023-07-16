package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.model.asNap
import com.bruno13palhano.core.data.database.model.asNapEntity
import com.bruno13palhano.model.Nap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NapRepository @Inject constructor(
    private val napDao: NapDao
) : CommonDataContract<Nap> {
    override suspend fun insert(model: Nap): Long {
        return napDao.insert(model.asNapEntity())
    }

    override fun getAll(): Flow<List<Nap>> {
        return napDao.getAll()
            .map {
                it.map { napEntity -> napEntity.asNap() }
            }
    }

    override fun getById(id: Long): Flow<Nap> {
        return napDao.getById(id)
            .map { it.asNap() }
            .catch { it.printStackTrace() }
    }

    override suspend fun deleteById(id: Long) {
        napDao.deleteById(id)
    }

    override suspend fun update(model: Nap) {
        napDao.update(model.asNapEntity())
    }

    override suspend fun delete(model: Nap) {
        napDao.delete(model.asNapEntity())
    }

    override fun getLast() = napDao.getLast()
        .map { it.asNap() }
        .catch {
            emit(
                Nap(
                    id= 0L,
                    title = "",
                    date = 0L,
                    startTime = 0L,
                    endTime = 0L,
                    sleepingTime = 0L,
                    observation = ""
                )
            )
        }
}