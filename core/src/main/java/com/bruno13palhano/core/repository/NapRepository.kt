package com.bruno13palhano.core.repository

import com.bruno13palhano.model.Nap
import kotlinx.coroutines.flow.Flow

interface NapRepository {
    suspend fun insert(nap: Nap): Long
    suspend fun update(nap: Nap)
    suspend fun deleteById(id: Long)
    suspend fun getById(id: Long): Nap?
    fun getAll(): Flow<List<Nap>>
    fun getLast(): Flow<Nap>
}
