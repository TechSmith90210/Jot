package com.mindpalace.app.data.repository

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.repository.UserRepository
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class UserRepositoryImpl(private val supabaseClient: SupabaseClient) : UserRepository {
    override suspend fun updateUserAvatarId(avatarId: String) {
        supabaseClient.client.auth.updateUser {
            data = buildJsonObject {
                put("avatar_id", JsonPrimitive(avatarId))
            }
        }
    }
}
