package com.mindpalace.app.di

import com.mindpalace.app.domain.repository.AuthRepository
import com.mindpalace.app.domain.repository.UserRepository
import com.mindpalace.app.domain.usecase.LoginUseCase
import com.mindpalace.app.domain.usecase.SignUpUseCase
import com.mindpalace.app.domain.usecase.UpdateUserAvatarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAvatarUpdateUseCase(userRepository: UserRepository): UpdateUserAvatarUseCase {
        return UpdateUserAvatarUseCase(userRepository)
    }
}