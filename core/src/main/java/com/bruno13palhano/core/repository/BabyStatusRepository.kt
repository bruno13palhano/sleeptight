package com.bruno13palhano.core.repository

import com.bruno13palhano.model.BabyStatus
import kotlinx.coroutines.flow.Flow

interface BabyStatusRepository {
    suspend fun insert(babyStatus: BabyStatus): Long
    suspend fun update(babyStatus: BabyStatus)
    suspend fun deleteById(id: Long)
    suspend fun getById(id: Long): BabyStatus?
    fun getAll(): Flow<List<BabyStatus>>
    fun getLast(): Flow<BabyStatus>
}
