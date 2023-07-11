package com.bruno13palhano.core.data.database.model

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
internal data class BabyStatusData(

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
    val weight: Float
)

/**
 * Transforms [BabyStatus] into [BabyStatusData].
 * @return [BabyStatusData].
 */
internal fun BabyStatus.asBabyStatusData() = BabyStatusData(
    id = id,
    title = title,
    date = date,
    height = height,
    weight = weight
)

/**
 * Transforms [BabyStatusData] into [BabyStatus].
 * @return [BabyStatus].
 */
internal fun BabyStatusData.asBabyStatus() = BabyStatus(
    id = id,
    title = title,
    date = date,
    height = height,
    weight = weight
)
