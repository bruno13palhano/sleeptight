package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.model.asNap
import com.bruno13palhano.core.data.database.model.asNapEntity
import com.bruno13palhano.core.data.di.ApplicationScope
import com.bruno13palhano.model.Nap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NapRepository @Inject constructor(
    private val napDao: NapDao,
    @ApplicationScope private val externalScope: CoroutineScope
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
        externalScope.launch {
            napDao.deleteById(id)
        }
    }

    override suspend fun update(model: Nap) {
        externalScope.launch {
            napDao.update(model.asNapEntity())
        }
    }

    override suspend fun delete(model: Nap) {
        externalScope.launch {
            napDao.delete(model.asNapEntity())
        }
    }

    override fun getLast() = napDao.getLast()
        .map { it.asNap() }
        .catch { it.printStackTrace() }
        .stateIn(
            scope = externalScope,
            started = WhileSubscribed(5_000),
            initialValue = Nap(
                id = 0L,
                title = "",
                date = 0L,
                startTime = 0L,
                endTime = 0L,
                sleepingTime = 0L,
                observation = ""
            )
        )
}