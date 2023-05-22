package com.bruno13palhano.core.data.repository

import kotlinx.coroutines.flow.Flow

interface RepositoryCrud<T> {
    suspend fun insert(model: T)
    suspend fun update(model: T)
    suspend fun deleteById(id: Long)
    fun getByIdStream(id: Long): Flow<T>
    fun getAllStream(): Flow<List<T>>
}