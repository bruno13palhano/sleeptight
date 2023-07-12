package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Nap

/**
 * Nap as an Entity.
 *
 * An entity class to persist the Nap in database.
 * @property id the id to identify this Nap.
 * @property title the title of this Nap.
 * @property date the date in milliseconds when this Nap occurred.
 * @property startTime the time this Nap started in milliseconds.
 * @property endTime the time this Nap ended in milliseconds.
 * @property sleepingTime the Nap duration in milliseconds.
 * @property observation the observations about this Nap.
 */
@Entity(tableName = "nap_table")
internal data class NapEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "start")
    val startTime: Long,

    @ColumnInfo(name = "sleeping_time")
    val sleepingTime: Long,

    @ColumnInfo(name = "end")
    val endTime: Long,

    @ColumnInfo(name = "observation")
    val observation: String
)

/**
 * Transforms [NapEntity] into [Nap].
 * @return [Nap].
 */
internal fun NapEntity.asNap() = Nap(
    id = id,
    title = title,
    date = date,
    startTime = startTime,
    endTime = endTime,
    sleepingTime = sleepingTime,
    observation = observation
)

/**
 * Transforms [Nap] into [NapEntity].
 * @return [NapEntity].
 */
internal fun Nap.asNapEntity() = NapEntity(
    id = id,
    title = title,
    date = date,
    startTime = startTime,
    endTime = endTime,
    sleepingTime = sleepingTime,
    observation = observation
)