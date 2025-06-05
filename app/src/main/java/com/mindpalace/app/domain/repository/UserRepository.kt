package com.mindpalace.app.domain.repository

interface UserRepository {
    suspend fun updateUserAvatarId(avatarId : String)
    suspend fun updateProfile(avatarId: String, displayName: String, bio: String?): Result<Unit>
}