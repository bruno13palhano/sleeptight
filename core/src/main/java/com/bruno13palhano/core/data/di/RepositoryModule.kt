package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import com.bruno13palhano.core.data.repository.NapRepository
import com.bruno13palhano.core.data.repository.NotificationRepository
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

/**
 * Annotation to inject [NapRepository].
 *
 * Injects the default [NapRepository] implementation.
 */
@Qualifier
annotation class NapRep

/**
 * Annotation to inject [UserRepository].
 *
 * Injects the default [UserRepository] implementation.
 */
@Qualifier
annotation class UserRep

/**
 * Annotation to inject [BabyStatusRepository].
 *
 * Injects the default [BabyStatusRepository] implementation.
 */
@Qualifier
annotation class BabyStatusRep

/**
 * Annotation to inject [NotificationRepository].
 *
 * Injects the default [NotificationRepository] implementation.
 */
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
        repository: BabyStatusRepository,
    ): CommonDataContract<BabyStatus>

    @NotificationRep
    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        repository: NotificationRepository,
    ): CommonDataContract<Notification>
}
