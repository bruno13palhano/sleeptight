package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.Nap
import kotlinx.coroutines.flow.Flow

interface NapRepository {
    suspend fun insert(nap: Nap)
    fun getAllStream(): Flow<List<Nap>>
}