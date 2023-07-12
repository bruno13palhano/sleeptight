package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.NapEntity
import kotlinx.coroutines.flow.Flow

/**
 * [NapEntity] Dao interface.
 *
 * A Data Access Object for the [NapEntity].
 * This interface is responsible for handling [NapEntity] access to the Room database.
 */
@Dao
internal interface NapDao : CommonDao<NapEntity> {

    /**
     * Gets all [NapEntity].
     * @return a [Flow] containing a [List] of all [NapEntity].
     */
    @Query("SELECT * FROM nap_table")
    fun getAllStream(): Flow<List<NapEntity>>

    /**
     * Gets the [NapEntity] specified by this [id].
     * @param id the [id] for this [NapEntity].
     * @return a [Flow] of [NapEntity].
     */
    @Query("SELECT * FROM nap_table WHERE id = :id")
    fun getNapByIdStream(id: Long): Flow<NapEntity>

    /**
     * Deletes the [NapEntity] specified by this [id].
     * @param id the [id] for this [NapEntity].
     */
    @Query("DELETE FROM nap_table WHERE id = :id")
    suspend fun deleteNapById(id: Long)

    /**
     * Gets the last [NapEntity] inserted into the database.
     * @return a [Flow] of [NapEntity].
     */
    @Query("SELECT * FROM nap_table WHERE id = (SELECT max(id) FROM nap_table)")
    fun getLastNapStream(): Flow<NapEntity>
}