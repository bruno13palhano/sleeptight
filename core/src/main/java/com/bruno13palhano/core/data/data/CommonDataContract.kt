package com.bruno13palhano.core.data.data

import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.model.Nap
import com.bruno13palhano.model.Notification
import kotlinx.coroutines.flow.Flow

/**
 * Contract for common data manipulation. e.g [Nap], [BabyStatus], [Notification].
 *
 * An interface to decouple the treatment of common data, regardless of concrete implementation.
 * @param T the model type.
 */
interface CommonDataContract<T> {
    suspend fun insert(model: T): Long
    suspend fun update(model: T)
    suspend fun delete(model: T)
    suspend fun deleteById(id: Long)
    fun getAll(): Flow<List<T>>
    fun getById(id: Long): Flow<T>
    fun getLast(): Flow<T>
}