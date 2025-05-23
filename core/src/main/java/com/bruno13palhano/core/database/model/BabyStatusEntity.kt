package com.bruno13palhano.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.BabyStatus

/**
 * BabyStatus as an Entity.
 *
 * An entity class to persist the BabyStatus in database.
 * @property id the id to identify this BabyStatus.
 * @property title the title of this BabyStatus.
 * @property date the date in milliseconds when this BabyStatus was measured.
 * @property height the height when this BabyStatus was measured.
 * @property weight the weight when this BabyStatus was measured.
 */
@Entity(tableName = "baby_status_table")
internal data class BabyStatusEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "height")
    val height: Float,

    @ColumnInfo(name = "weight")
    val weight: Float,
)

/**
 * Transforms [BabyStatus] into [BabyStatusEntity].
 * @return [BabyStatusEntity].
 */
internal fun BabyStatus.asBabyStatusEntity() = BabyStatusEntity(
    id = id,
    title = title,
    date = date,
    height = height,
    weight = weight,
)

/**
 * Transforms [BabyStatusEntity] into [BabyStatus].
 * @return [BabyStatus].
 */
internal fun BabyStatusEntity.asBabyStatus() = BabyStatus(
    id = id,
    title = title,
    date = date,
    height = height,
    weight = weight,
)
