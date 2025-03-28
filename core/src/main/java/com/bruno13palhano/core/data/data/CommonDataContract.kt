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

    /**
     * Insert a [model] of type [T].
     * @param model the new [model] to be inserted.
     */
    suspend fun insert(model: T): Long

    /**
     * Updates the [model] of type [T].
     * @param model the [model] to be updated.
     */
    suspend fun update(model: T)

    /**
     * Deletes the [model] of type [T].
     * @param model the [model] to be deleted.
     */
    suspend fun delete(model: T)

    /**
     * Deletes the model of type [T] specified by this [id].
     * @param id the [id] of the model to be deleted.
     */
    suspend fun deleteById(id: Long)

    /**
     * Gets all models of type [T].
     * @return a [Flow] containing a [List] of all models of type [T].
     */
    fun getAll(): Flow<List<T>>

    /**
     * Gets the model of type [T] specified by this [id].
     * @param id the [id] for this model of type [T].
     * @return a [Flow] containing a model of type [T].
     */
    fun getById(id: Long): Flow<T>

    /**
     * Gets the last model [T] inserted.
     * @return a [Flow] containing a model of type [T].
     */
    fun getLast(): Flow<T>
}
