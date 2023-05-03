package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.BabyStatus

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

internal fun BabyStatus.asBabyStatusData() = BabyStatusData(
    id = id,
    title = title,
    date = date,
    height = height,
    weight = weight
)

internal fun BabyStatusData.asBabyStatus() = BabyStatus(
    id = id,
    title = title,
    date = date,
    height = height,
    weight = weight
)
