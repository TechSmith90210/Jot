package com.mindpalace.app.domain.usecase.user

import com.mindpalace.app.domain.repository.UserRepository

class UpdateUserProfileUseCase(private val repository: UserRepository) {
    suspend fun execute(avatarId: String, displayName: String, bio: String?): Result<Unit>{
        return repository.updateProfile(avatarId, displayName, bio)
    }
}