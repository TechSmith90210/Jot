package com.mindpalace.app.di

import com.mindpalace.app.domain.repository.AuthRepository
import com.mindpalace.app.domain.repository.MindFragmentRepository
import com.mindpalace.app.domain.repository.UserRepository
import com.mindpalace.app.domain.usecase.mind_fragment.CreateFragmentUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.DeleteFragmentUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.GetAllFragmentsUseCase
import com.mindpalace.app.domain.usecase.auth.GetCurrentUserUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.GetFragmentUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.GetFragmentsByCreatedAtUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.GetFragmentsByLastOpenedUseCase
import com.mindpalace.app.domain.usecase.auth.GoogleSignInUseCase
import com.mindpalace.app.domain.usecase.auth.LoginUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.MindFragmentUseCases
import com.mindpalace.app.domain.usecase.auth.SignUpUseCase
import com.mindpalace.app.domain.usecase.mind_fragment.UpdateFragmentUseCase
import com.mindpalace.app.domain.usecase.user.UpdateUserAvatarUseCase
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
    fun provideGoogleSignInUseCase(authRepository: AuthRepository): GoogleSignInUseCase {
        return GoogleSignInUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAvatarUpdateUseCase(userRepository: UserRepository): UpdateUserAvatarUseCase {
        return UpdateUserAvatarUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideMindFragmentUseCases(
        repository: MindFragmentRepository
    ): MindFragmentUseCases {
        return MindFragmentUseCases(
            createFragment = CreateFragmentUseCase(repository),
            getFragmentsByLastOpened = GetFragmentsByLastOpenedUseCase(repository),
            getFragmentsByCreatedAt = GetFragmentsByCreatedAtUseCase(repository),
            getAllFragments = GetAllFragmentsUseCase(repository),
            updateFragment = UpdateFragmentUseCase(repository),
            deleteFragment = DeleteFragmentUseCase(repository),
            getFragment = GetFragmentUseCase(repository)
        )
    }
}