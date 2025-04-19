package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}