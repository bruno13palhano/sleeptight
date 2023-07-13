package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.database.model.BabyStatusEntity
import kotlinx.coroutines.flow.Flow

/**
 * [BabyStatusEntity] Dao interface.
 *
 * A Data Access Object for the [BabyStatusEntity] model.
 * This interface is responsible for handling [BabyStatusEntity] access to the Room database.
 */
@Dao
internal interface BabyStatusDao : CommonDataContract<BabyStatusEntity> {

    /**
     * Inserts a [BabyStatusEntity] into the database.
     * @param model the new [BabyStatusEntity].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: BabyStatusEntity): Long

    /**
     * Updates the [BabyStatusEntity] in the database.
     * @param model the [BabyStatusEntity] to be updated.
     */
    @Update
    override suspend fun update(model: BabyStatusEntity)

    /**
     * Deletes the [BabyStatusEntity] in the database.
     * @param model the [BabyStatusEntity] to be deleted.
     */
    @Delete
    override suspend fun delete(model: BabyStatusEntity)

    /**
     * Gets all [BabyStatusEntity].
     * @return a [Flow] containing a [List] of all [BabyStatusEntity].
     */
    @Query("SELECT * FROM baby_status_table")
    override fun getAll(): Flow<List<BabyStatusEntity>>

    /**
     * Gets the [BabyStatusEntity] specified by this [id].
     * @param id the [id] for this [BabyStatusEntity].
     * @return a [Flow] of [BabyStatusEntity].
     */
    @Query("SELECT * FROM baby_status_table WHERE id = :id")
    override fun getById(id: Long): Flow<BabyStatusEntity>

    /**
     * Deletes the [BabyStatusEntity] specified by this [id].
     * @param id the [id] for this [BabyStatusEntity].
     */
    @Query("DELETE FROM baby_status_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    /**
     * Gets the last [BabyStatusEntity] inserted into the database.
     * @return a [Flow] of [BabyStatusEntity].
     */
    @Query("SELECT * FROM baby_status_table WHERE id = (SELECT max(id) FROM " +
            "baby_status_table)")
    override fun getLast(): Flow<BabyStatusEntity>
}