package com.bruno13palhano.core.data.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.core.data.database.dao.NapDao
import com.bruno13palhano.core.data.database.SleepTightDatabase
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