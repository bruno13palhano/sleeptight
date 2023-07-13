package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.database.data.CommonDataContract
import com.bruno13palhano.core.data.database.model.NapEntity
import kotlinx.coroutines.flow.Flow

/**
 * [NapEntity] Dao interface.
 *
 * A Data Access Object for the [NapEntity].
 * This interface is responsible for handling [NapEntity] access to the Room database.
 */
@Dao
internal interface NapDao : CommonDataContract<NapEntity> {

    /**
     * Inserts a [NapEntity] into the database.
     * @param model the new [NapEntity].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: NapEntity): Long

    /**
     * Updates the [NapEntity] in the database.
     * @param model the [NapEntity] to be updated.
     */
    @Update
    override suspend fun update(model: NapEntity)

    /**
     * Deletes the [NapEntity] in the database.
     * @param model the [NapEntity] to be updated.
     */
    @Delete
    override suspend fun delete(model: NapEntity)

    /**
     * Gets all [NapEntity].
     * @return a [Flow] containing a [List] of all [NapEntity].
     */
    @Query("SELECT * FROM nap_table")
    override fun getAll(): Flow<List<NapEntity>>

    /**
     * Gets the [NapEntity] specified by this [id].
     * @param id the [id] for this [NapEntity].
     * @return a [Flow] of [NapEntity].
     */
    @Query("SELECT * FROM nap_table WHERE id = :id")
    override fun getById(id: Long): Flow<NapEntity>

    /**
     * Deletes the [NapEntity] specified by this [id].
     * @param id the [id] for this [NapEntity].
     */
    @Query("DELETE FROM nap_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    /**
     * Gets the last [NapEntity] inserted into the database.
     * @return a [Flow] of [NapEntity].
     */
    @Query("SELECT * FROM nap_table WHERE id = (SELECT max(id) FROM nap_table)")
    override fun getLast(): Flow<NapEntity>
}