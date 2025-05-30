package com.mindpalace.app.domain.usecase.user

import com.mindpalace.app.domain.repository.UserRepository

class UpdateUserAvatarUseCase(private val userRepository: UserRepository) {
    suspend fun execute(avatarId: String): Result<Unit> {
        return try {
            userRepository.updateUserAvatarId(avatarId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}