package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.*
import com.bruno13palhano.core.data.repository.DefaultNapRepository
import com.bruno13palhano.core.data.repository.DefaultUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DefaultNapRep

@Qualifier
annotation class DefaultUserRep

@Qualifier
annotation class DefaultBabyStatusRep

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @DefaultNapRep
    @Singleton
    @Binds
    abstract fun bindDefaultNapRepository(repository: DefaultNapRepository): NapRepository

    @DefaultUserRep
    @Singleton
    @Binds
    abstract fun bindDefaultUserRepository(repository: DefaultUserRepository): UserRepository

    @DefaultBabyStatusRep
    @Singleton
    @Binds
    abstract fun bindDefaultBabyStateRepository(repository: DefaultBabyStatusRepository): BabyStatusRepository
}