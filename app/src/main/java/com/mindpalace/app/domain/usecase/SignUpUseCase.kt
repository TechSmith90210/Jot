package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.AuthRepository

class SignUpUseCase (private val authRepository: AuthRepository) {
    suspend fun execute (email:String, password:String): Result<User> {
        return authRepository.register(email = email, password = password)
    }
}