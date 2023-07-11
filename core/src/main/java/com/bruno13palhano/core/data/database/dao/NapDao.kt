package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.NapData
import kotlinx.coroutines.flow.Flow

/**
 * [NapData] Dao interface.
 *
 * A Data Access Object for the [NapData].
 * This interface is responsible for handling [NapData] access to the Room database.
 */
@Dao
internal interface NapDao : CommonDao<NapData> {

    /**
     * Gets all [NapData].
     * @return a [Flow] containing a [List] of all [NapData].
     */
    @Query("SELECT * FROM nap_table")
    fun getAllStream(): Flow<List<NapData>>

    /**
     * Gets the [NapData] specified by this [id].
     * @param id the [id] for this [NapData].
     * @return a [Flow] of [NapData].
     */
    @Query("SELECT * FROM nap_table WHERE id = :id")
    fun getNapByIdStream(id: Long): Flow<NapData>

    /**
     * Deletes the [NapData] specified by this [id].
     * @param id the [id] for this [NapData].
     */
    @Query("DELETE FROM nap_table WHERE id = :id")
    suspend fun deleteNapById(id: Long)

    /**
     * Gets the last [NapData] inserted into the database.
     * @return a [Flow] of [NapData].
     */
    @Query("SELECT * FROM nap_table WHERE id = (SELECT max(id) FROM nap_table)")
    fun getLastNapStream(): Flow<NapData>
}