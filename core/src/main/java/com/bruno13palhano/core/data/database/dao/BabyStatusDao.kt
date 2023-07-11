package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.BabyStatusData
import kotlinx.coroutines.flow.Flow

/**
 * [BabyStatusData] Dao interface.
 *
 * A Data Access Object for the [BabyStatusData] model.
 * This interface is responsible for handling [BabyStatusData] access to the Room database.
 */
@Dao
internal interface BabyStatusDao : CommonDao<BabyStatusData>{

    /**
     * Gets all [BabyStatusData].
     * @return a [Flow] containing a [List] of all [BabyStatusData].
     */
    @Query("SELECT * FROM baby_status_table")
    fun getAllStream(): Flow<List<BabyStatusData>>

    /**
     * Gets the [BabyStatusData] specified by this [id].
     * @param id the [id] for this [BabyStatusData].
     * @return a [Flow] of [BabyStatusData].
     */
    @Query("SELECT * FROM baby_status_table WHERE id = :id")
    fun getBabyStatusByIdStream(id: Long): Flow<BabyStatusData>

    /**
     * Deletes the [BabyStatusData] specified by this [id].
     * @param id the [id] for this [BabyStatusData].
     */
    @Query("DELETE FROM baby_status_table WHERE id = :id")
    suspend fun deleteBabyStatusById(id: Long)

    /**
     * Gets the last [BabyStatusData] inserted into the database.
     * @return a [Flow] of [BabyStatusData].
     */
    @Query("SELECT * FROM baby_status_table WHERE id = (SELECT max(id) FROM " +
            "baby_status_table)")
    fun getLastBabyStatusStream(): Flow<BabyStatusData>
}