package com.mindpalace.app.domain.usecase.user

import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.UserRepository

class GetUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): Result<User> {
        return userRepository.getProfile(userId)
    }
}