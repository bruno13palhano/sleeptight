package com.bruno13palhano.core.data.repository

import kotlinx.coroutines.flow.Flow

interface CommonRepository<T> {
    suspend fun insert(model: T): Long
    suspend fun update(model: T)
    suspend fun deleteById(id: Long)
    fun getByIdStream(id: Long): Flow<T>
    fun getAllStream(): Flow<List<T>>
    fun getLastStream(): Flow<T>
}