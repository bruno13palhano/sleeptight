package com.bruno13palhano.core.data.repository

import com.bruno13palhano.model.BabyStatus
import kotlinx.coroutines.flow.Flow

interface BabyStatusRepository : RepositoryCrud<BabyStatus> {
    fun getLastBabyStatusStream(): Flow<BabyStatus>
}