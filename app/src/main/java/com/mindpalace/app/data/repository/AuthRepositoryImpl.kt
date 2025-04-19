package com.mindpalace.app.data.repository

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepositoryImpl(supabaseCli: SupabaseClient) : AuthRepository {

    private val auth: Auth = supabaseCli.client.auth

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {
            auth.signInWith(provider = Email) {
                this.email = email
                this.password = password
            }

            val user = auth.currentUserOrNull() ?.let {
                User(
                    id = it.id,
                    email = it.email ?: "",
                    avatarId = it.userMetadata?.get("avatar_id") as? String ?: "",
                    createdAt = it.createdAt.toString()
                )
            }

            // If user is found, return Result.success(), otherwise return Result.failure()
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<User> {
        return try {
            auth.signUpWith(provider = Email) {
                this.email = email
                this.password = password
            }

            val user = auth.currentUserOrNull()?.let {
                User(
                    id = it.id,
                    email = it.email ?: "",
                    avatarId = "",
                    createdAt = it.createdAt.toString()
                )
            }

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed: User data is null"))
            }
        } catch (e: Exception) {
            // Provide a more meaningful error message
            Result.failure(Exception("Registration failed: ${e.message}", e))
        }
    }
}
