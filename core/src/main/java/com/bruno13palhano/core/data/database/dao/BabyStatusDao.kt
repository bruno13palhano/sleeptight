package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bruno13palhano.core.data.database.model.BabyStatusEntity
import kotlinx.coroutines.flow.Flow

/**
 * [BabyStatusEntity] Dao interface.
 *
 * A Data Access Object for the [BabyStatusEntity] model.
 * This interface is responsible for handling [BabyStatusEntity] access to the Room database.
 */
@Dao
internal interface BabyStatusDao : CommonDao<BabyStatusEntity>{

    /**
     * Gets all [BabyStatusEntity].
     * @return a [Flow] containing a [List] of all [BabyStatusEntity].
     */
    @Query("SELECT * FROM baby_status_table")
    fun getAllStream(): Flow<List<BabyStatusEntity>>

    /**
     * Gets the [BabyStatusEntity] specified by this [id].
     * @param id the [id] for this [BabyStatusEntity].
     * @return a [Flow] of [BabyStatusEntity].
     */
    @Query("SELECT * FROM baby_status_table WHERE id = :id")
    fun getBabyStatusByIdStream(id: Long): Flow<BabyStatusEntity>

    /**
     * Deletes the [BabyStatusEntity] specified by this [id].
     * @param id the [id] for this [BabyStatusEntity].
     */
    @Query("DELETE FROM baby_status_table WHERE id = :id")
    suspend fun deleteBabyStatusById(id: Long)

    /**
     * Gets the last [BabyStatusEntity] inserted into the database.
     * @return a [Flow] of [BabyStatusEntity].
     */
    @Query("SELECT * FROM baby_status_table WHERE id = (SELECT max(id) FROM " +
            "baby_status_table)")
    fun getLastBabyStatusStream(): Flow<BabyStatusEntity>
}