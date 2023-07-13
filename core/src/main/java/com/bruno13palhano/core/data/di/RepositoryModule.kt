package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.repository.*
import com.bruno13palhano.core.data.repository.DefaultNapRepository
import com.bruno13palhano.core.data.repository.DefaultUserRepository
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.model.Nap
import com.bruno13palhano.model.Notification
import com.bruno13palhano.model.User
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

@Qualifier
annotation class DefaultNotificationRep

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @DefaultNapRep
    @Singleton
    @Binds
    abstract fun bindDefaultNapRepository(
        repository: DefaultNapRepository
    ): CommonDataContract<Nap>

    @DefaultUserRep
    @Singleton
    @Binds
    abstract fun bindDefaultUserRepository(
        repository: DefaultUserRepository
    ): UserDataContract<User>

    @DefaultBabyStatusRep
    @Singleton
    @Binds
    abstract fun bindDefaultBabyStateRepository(
        repository: DefaultBabyStatusRepository
    ): CommonDataContract<BabyStatus>

    @DefaultNotificationRep
    @Singleton
    @Binds
    abstract fun bindDefaultNotificationRepository(
        repository: DefaultNotificationRepository
    ): CommonDataContract<Notification>
}