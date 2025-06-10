package com.mindpalace.app.domain.usecase.auth

import com.mindpalace.app.domain.repository.AuthRepository

class SignOutUserUseCase(private val authRepository: AuthRepository) {
    suspend fun execute() {
        authRepository.signOutUser()
    }
}