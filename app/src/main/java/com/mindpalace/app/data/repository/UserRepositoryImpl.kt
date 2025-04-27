package com.mindpalace.app.data.repository

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.repository.UserRepository
import io.github.jan.supabase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import io.ktor.client.engine.cio.*
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json() // Automatically handle JSON
    }
}

@Serializable
data class RandomUserResponse(
    val usernames: List<String>
)

suspend fun fetchRandomUserName(): String {
    val response: RandomUserResponse =
        client.get("https://usernameapiv1.vercel.app/api/random-usernames").body()
    return response.usernames.firstOrNull() ?: "random_name"
}

class UserRepositoryImpl(private val supabaseClient: SupabaseClient) : UserRepository {
    override suspend fun updateUserAvatarId(avatarId: String) {
        val username = fetchRandomUserName()

        supabaseClient.client.auth.updateUser {
            data = buildJsonObject {
                put("avatar_id", JsonPrimitive(avatarId))
                put("display_name", JsonPrimitive(username))
            }
        }
    }
}
