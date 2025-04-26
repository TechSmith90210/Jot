package com.mindpalace.app.domain.usecase

import android.content.Context
import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.AuthRepository

class GoogleSignInUseCase (private val authRepository : AuthRepository) {
    suspend fun invoke(context: Context) : Result<User> {
        return authRepository.signInWithGoogle(context)
    }
}