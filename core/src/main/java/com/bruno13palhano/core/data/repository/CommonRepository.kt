package com.bruno13palhano.core.data.repository

import kotlinx.coroutines.flow.Flow

interface CommonRepository<T> {
    suspend fun insert(model: T): Long
    fun update(model: T)
    fun deleteById(id: Long)
    fun getByIdStream(id: Long): Flow<T>
    val all: Flow<List<T>>
    val last: Flow<T>
}