// core/di/RepositoryModule.kt
package com.buildndeploy.raah.core.di

import com.buildndeploy.raah.data.repository.ActivityRepositoryImpl
import com.buildndeploy.raah.data.repository.AuthRepositoryImpl
import com.buildndeploy.raah.domain.repository.ActivityRepository
import com.buildndeploy.raah.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(
        activityRepositoryImpl: ActivityRepositoryImpl
    ): ActivityRepository
}
