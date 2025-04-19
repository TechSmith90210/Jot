package com.mindpalace.app.domain.repository

import com.mindpalace.app.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String): Result<User>
}