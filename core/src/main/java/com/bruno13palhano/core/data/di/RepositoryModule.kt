package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.repository.*
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.core.data.repository.UserRepository
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
annotation class NapRep

@Qualifier
annotation class UserRep

@Qualifier
annotation class BabyStatusRep

@Qualifier
annotation class NotificationRep

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @NapRep
    @Singleton
    @Binds
    abstract fun bindNapRepository(repository: NapRepository): CommonDataContract<Nap>

    @UserRep
    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: UserRepository): UserDataContract<User>

    @BabyStatusRep
    @Singleton
    @Binds
    abstract fun bindBabyStateRepository(
        repository: BabyStatusRepository
    ): CommonDataContract<BabyStatus>

    @NotificationRep
    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        repository: NotificationRepository
    ): CommonDataContract<Notification>
}