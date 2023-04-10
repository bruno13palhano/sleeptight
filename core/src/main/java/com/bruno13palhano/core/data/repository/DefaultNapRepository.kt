package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.NapDao
import com.bruno13palhano.core.data.database.asNap
import com.bruno13palhano.core.data.database.asNapData
import com.bruno13palhano.model.Nap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultNapRepository @Inject constructor(
    private val napDao: NapDao
) : NapRepository {
    override suspend fun insert(nap: Nap) {
        napDao.insert(nap.asNapData())
    }

    override fun getAllStream(): Flow<List<Nap>> {
        return napDao.getAllStream().map {
            it.map { nap ->
                nap.asNap()
            }
        }
    }
}