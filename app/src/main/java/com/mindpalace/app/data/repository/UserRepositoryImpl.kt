package com.mindpalace.app.data.repository

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.UserRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            })
    }
}

@Serializable
data class RandomUserLogin(
    val username: String
)

@Serializable
data class RandomUserResult(
    val login: RandomUserLogin
)

@Serializable
data class RandomUserResponse(
    val results: List<RandomUserResult>
)

suspend fun fetchRandomUserName(): String {
    val response: RandomUserResponse = client.get("https://randomuser.me/api/").body()
    return response.results.firstOrNull()?.login?.username ?: "random_user"
}

class UserRepositoryImpl(private val supabaseClient: SupabaseClient) : UserRepository {
    override suspend fun updateUserAvatarId(avatarId: String) {
        val username = fetchRandomUserName()
        val uid = supabaseClient.client.auth.currentUserOrNull()?.id

        supabaseClient.client.from("users").update(
            {
                User::displayName setTo username
                User::avatarId setTo avatarId
            }) {
            filter {
                User::id eq uid
            }
        }

    }

    override suspend fun updateProfile(
        avatarId: String, displayName: String, bio: String?
    ): Result<Unit> {
        val uid = supabaseClient.client.auth.currentUserOrNull()?.id
        return try {
            supabaseClient.client.from("users").update(
                {
                    User::avatarId setTo avatarId
                    User::displayName setTo displayName
                    User::bio setTo bio
                }) {
                filter {
                    User::id eq uid
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

//fun main() = runBlocking {
//    val username = fetchRandomUserName()
//    println("Random Username : $username")
//}