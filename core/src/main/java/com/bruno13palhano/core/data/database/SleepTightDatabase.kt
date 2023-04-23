package com.bruno13palhano.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.dao.UserDao
import com.bruno13palhano.core.data.database.model.NapData
import com.bruno13palhano.core.data.database.model.UserData

@Database(
    entities = [
        NapData::class,
        UserData::class,
    ],
    version = 1,
    exportSchema = false
)
internal abstract class SleepTightDatabase : RoomDatabase() {
    abstract val napDao: NapDao
    abstract val userDao: UserDao
}