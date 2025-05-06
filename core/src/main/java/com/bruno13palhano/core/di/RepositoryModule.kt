package com.bruno13palhano.core.di

import com.bruno13palhano.core.repository.BabyStatusRepository
import com.bruno13palhano.core.repository.BabyStatusRepositoryImpl
import com.bruno13palhano.core.repository.NapRepository
import com.bruno13palhano.core.repository.NapRepositoryImpl
import com.bruno13palhano.core.repository.NotificationRepository
import com.bruno13palhano.core.repository.NotificationRepositoryImpl
import com.bruno13palhano.core.repository.UserRepository
import com.bruno13palhano.core.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Annotation to inject [NapRepositoryImpl].
 *
 * Injects the default [NapRepositoryImpl] implementation.
 */
@Qualifier
annotation class NapRep

/**
 * Annotation to inject [UserRepositoryImpl].
 *
 * Injects the default [UserRepositoryImpl] implementation.
 */
@Qualifier
annotation class UserRep

/**
 * Annotation to inject [BabyStatusRepositoryImpl].
 *
 * Injects the default [BabyStatusRepositoryImpl] implementation.
 */
@Qualifier
annotation class BabyStatusRep

/**
 * Annotation to inject [NotificationRepositoryImpl].
 *
 * Injects the default [NotificationRepositoryImpl] implementation.
 */
@Qualifier
annotation class NotificationRep

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @NapRep
    @Singleton
    @Binds
    abstract fun bindNapRepository(repository: NapRepositoryImpl): NapRepository

    @UserRep
    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @BabyStatusRep
    @Singleton
    @Binds
    abstract fun bindBabyStateRepository(
        repository: BabyStatusRepositoryImpl,
    ): BabyStatusRepository

    @NotificationRep
    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        repository: NotificationRepositoryImpl,
    ): NotificationRepository
}
