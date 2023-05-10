package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Nap

@Entity(tableName = "nap_table")
internal data class NapData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "start")
    val startTime: Long,

    @ColumnInfo(name = "sleep_time")
    val sleepTime: Long,

    @ColumnInfo(name = "end")
    val endTime: Long,

    @ColumnInfo(name = "observation")
    val observation: String
)

internal fun NapData.asNap() = Nap(
    id = id,
    title = title,
    date = date,
    startTime = startTime,
    endTime = endTime,
    sleepTime = sleepTime,
    observation = observation
)

internal fun Nap.asNapData() = NapData(
    id = id,
    title = title,
    date = date,
    startTime = startTime,
    endTime = endTime,
    sleepTime = sleepTime,
    observation = observation
)