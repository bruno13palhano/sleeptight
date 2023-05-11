package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.BabyStatus
import kotlinx.coroutines.flow.Flow

interface BabyStatusRepository {
    suspend fun insertBabyStatus(babyStatus: BabyStatus)
    fun getAllStream(): Flow<List<BabyStatus>>
    fun getBabyStatusByIdStream(id: Long): Flow<BabyStatus>
    suspend fun updateBabyStatus(babyStatus: BabyStatus)
    suspend fun deleteBabyStatusById(id: Long)
    fun getLastBabyStatusStream(): Flow<BabyStatus>
}