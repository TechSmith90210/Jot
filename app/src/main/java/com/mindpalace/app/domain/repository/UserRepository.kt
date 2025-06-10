package com.mindpalace.app.domain.repository

import com.mindpalace.app.domain.model.User

interface UserRepository {
    suspend fun updateUserAvatarId(avatarId : String)
    suspend fun updateProfile(avatarId: String, displayName: String, bio: String?): Result<Unit>
    suspend fun getProfile(
        userId: String,
    ): Result<User>
}