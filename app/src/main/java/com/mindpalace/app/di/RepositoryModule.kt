package com.mindpalace.app.di

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.data.repository.AuthRepositoryImpl
import com.mindpalace.app.data.repository.UserRepositoryImpl
import com.mindpalace.app.domain.repository.AuthRepository
import com.mindpalace.app.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(supabaseClient: SupabaseClient): AuthRepository {
        return AuthRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideUserRepository(supabaseClient: SupabaseClient): UserRepository {
        return UserRepositoryImpl(supabaseClient)
    }
}