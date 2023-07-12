package com.bruno13palhano.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.core.data.database.dao.BabyStatusDao
import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.dao.NotificationDao
import com.bruno13palhano.core.data.database.dao.UserDao
import com.bruno13palhano.core.data.database.model.BabyStatusEntity
import com.bruno13palhano.core.data.database.model.NapEntity
import com.bruno13palhano.core.data.database.model.NotificationEntity
import com.bruno13palhano.core.data.database.model.UserEntity

@Database(
    entities = [
        NapEntity::class,
        UserEntity::class,
        BabyStatusEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class SleepTightDatabase : RoomDatabase() {
    abstract val napDao: NapDao
    abstract val userDao: UserDao
    abstract val babyStatusDao: BabyStatusDao
    abstract val notificationDao: NotificationDao
}