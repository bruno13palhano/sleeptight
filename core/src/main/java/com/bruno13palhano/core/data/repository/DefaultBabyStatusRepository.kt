package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.dao.BabyStatusDao
import com.bruno13palhano.core.data.database.model.asBabyStatus
import com.bruno13palhano.core.data.database.model.asBabyStatusData
import com.bruno13palhano.core.data.di.ApplicationScope
import com.bruno13palhano.model.BabyStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultBabyStatusRepository @Inject constructor(
    private val babyStatusDao: BabyStatusDao,
    @ApplicationScope private val externalScope: CoroutineScope
) : BabyStatusRepository {
    override suspend fun insert(model: BabyStatus): Long {
        return babyStatusDao.insert(model.asBabyStatusData())
    }

    override val all: Flow<List<BabyStatus>> = babyStatusDao.getAllStream()
        .map {
            it.map { babyStatusData -> babyStatusData.asBabyStatus() }
        }
        .stateIn(
            scope = externalScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    override fun getByIdStream(id: Long): Flow<BabyStatus> {
        return babyStatusDao.getBabyStatusByIdStream(id)
            .map { it.asBabyStatus() }
            .catch { it.printStackTrace() }
    }

    override fun update(model: BabyStatus) {
        externalScope.launch {
            babyStatusDao.update(model.asBabyStatusData())
        }
    }

    override fun deleteById(id: Long) {
        externalScope.launch {
            babyStatusDao.deleteBabyStatusById(id)
        }
    }

    override val last: Flow<BabyStatus> = babyStatusDao.getLastBabyStatusStream()
        .map { it.asBabyStatus() }
        .catch { it.printStackTrace() }
        .stateIn(
            scope = externalScope,
            started = WhileSubscribed(5_000),
            initialValue = BabyStatus(
                id = 0L,
                title = "",
                date = 0L,
                height = 0F,
                weight = 0F
            )
        )
}