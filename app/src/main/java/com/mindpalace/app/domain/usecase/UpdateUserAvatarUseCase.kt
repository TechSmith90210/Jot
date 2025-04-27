package com.mindpalace.app.domain.usecase

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.UserRepository
import io.github.jan.supabase.auth.auth

class UpdateUserAvatarUseCase(private val userRepository: UserRepository  ) {

    suspend fun execute(avatarId: String): Result<User> {
        return try {
            // Get the current authenticated user using SupabaseClient directly
            val user = SupabaseClient.client.auth.currentUserOrNull()

            if (user != null) {
                // Update avatar ID
                userRepository.updateUserAvatarId(avatarId)

                // Create a User object and return as Result.success
                val updatedUser = User(
                    id = user.id,
                    email = user.email ?: "",
                    createdAt = (user.createdAt ?: "").toString(),
                    avatarId = avatarId,
                    lastSignInAt = (user.lastSignInAt ?: "").toString(),
                    displayName = user.userMetadata?.get("display_name") as? String ?: "",
                )

                Result.success(updatedUser)
            } else {
                Result.failure(Exception("User not found or not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
