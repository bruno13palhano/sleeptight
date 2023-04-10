package com.bruno13palhano.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NapData::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class SleepTightDatabase : RoomDatabase() {
    abstract val napDao: NapDao
}