package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}