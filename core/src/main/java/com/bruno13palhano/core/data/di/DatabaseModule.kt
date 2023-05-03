package com.bruno13palhano.core.data.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.SleepTightDatabase
import com.bruno13palhano.core.data.database.dao.BabyStatusDao
import com.bruno13palhano.core.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {

    @Provides
    fun providesNapDao(database: SleepTightDatabase): NapDao {
        return database.napDao
    }

    @Provides
    fun providesUserDao(database: SleepTightDatabase): UserDao {
        return database.userDao
    }

    @Provides
    fun providesBabyStatusDao(database: SleepTightDatabase): BabyStatusDao {
        return database.babyStatusDao
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): SleepTightDatabase {
        return Room.databaseBuilder(
            appContext,
            SleepTightDatabase::class.java,
            "sleep_tight_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}