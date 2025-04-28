package com.mindpalace.app.data.repository

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.mindpalace.app.BuildConfig
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.model.User
import com.mindpalace.app.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

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

            val user = auth.currentUserOrNull()?.let {
                User(
                    id = it.id,
                    email = it.email ?: "",
                    avatarId = it.userMetadata?.get("avatar_id") as? String ?: "",
                    createdAt = it.createdAt.toString(),
                    lastSignInAt = it.lastSignInAt.toString(),
                    displayName = it.userMetadata?.get("display_name") as? String ?: "",
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
                    createdAt = it.createdAt.toString(),
                    lastSignInAt = it.lastSignInAt.toString(),
                    displayName = it.userMetadata?.get("display_name") as? String ?: "",
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

    override suspend fun signInWithGoogle(context: Context): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val credentialManager = androidx.credentials.CredentialManager.create(context)
                val rawNonce = UUID.randomUUID().toString()
                val hashedNonce = MessageDigest.getInstance("SHA-256")
                    .digest(rawNonce.toByteArray())
                    .joinToString("") { "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setNonce(hashedNonce)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(credentialOption = googleIdOption)
                    .build()

                val result = credentialManager.getCredential(request = request, context = context)
                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = credential.idToken

                auth.signInWith(IDToken) {
                    this.idToken = idToken
                    provider = Google
                    nonce = rawNonce
                }

                auth.currentUserOrNull()?.let {
                    Result.success(
                        User(
                            id = it.id,
                            email = it.email ?: "",
                            avatarId = "",
                            createdAt = it.createdAt.toString(),
                            lastSignInAt = it.lastSignInAt.toString(),
                            displayName = it.userMetadata?.get("display_name") as? String ?: "",
                        )
                    )
                } ?: Result.failure(Exception("User is null"))

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getCurrentUser(): User? {
        val query = auth.currentUserOrNull() ?: return null // If no user, return null immediately
        val rawUserMetaData = query.userMetadata

        val avatarId = rawUserMetaData?.get("avatar_id")?.toString()?.replace("\"", "")?.trim() ?: "Default Avatar"
        val displayName = rawUserMetaData?.get("display_name")?.toString()?.replace("\"", "")?.trim() ?: "Default User"

        val data = User(
            id = query.id,
            email = query.email ?: "",
            avatarId = avatarId,
            createdAt = query.createdAt.toString(),
            lastSignInAt = query.lastSignInAt.toString(),
            displayName = displayName,
        )
        println("real data: $data")
        return data
    }


}
