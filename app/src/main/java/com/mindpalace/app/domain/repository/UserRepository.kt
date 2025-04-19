package com.mindpalace.app.domain.repository

interface UserRepository {
    suspend fun updateUserAvatarId(avatarId : String)
}