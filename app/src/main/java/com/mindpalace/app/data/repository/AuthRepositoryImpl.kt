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
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

class AuthRepositoryImpl(supabaseCli: SupabaseClient) : AuthRepository {

    private val auth: Auth = supabaseCli.client.auth
    private val supcli = supabaseCli.client

    override suspend fun login(
        email: String, password: String
    ): Result<User> {
        return try {
            auth.signInWith(provider = Email) {
                this.email = email
                this.password = password
            }

            val currentUserId =
                auth.currentUserOrNull()?.id ?: return Result.failure(Exception("User is null"))

            val query = supcli.from("users").select {
                filter {
                    User::id eq currentUserId
                }
            }.decodeSingleOrNull<User>()

            val user = query?.let {
                User(
                    id = it.id,
                    email = it.email,
                    avatarId = it.avatarId,
                    createdAt = it.createdAt,
                    lastSignedIn = it.lastSignedIn,
                    displayName = it.displayName,
                    bio = it.bio
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
        email: String, password: String
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
                    createdAt = it.createdAt.toString(),
                    lastSignedIn = it.lastSignInAt.toString(),
                    avatarId = "",
                    displayName = "",
                    bio = ""
                )
            }

            if (user == null) {
                return Result.failure(Exception("Registration failed: User is null"))
            }

            val actualUser =
                supcli.from("users").insert(user) { select() }.decodeSingleOrNull<User>()

            if (actualUser != null) {
                Result.success(actualUser)
            } else {
                Result.failure(Exception("Registration failed: User data is null"))
            }
        } catch (e: Exception) {
            // Provide a more meaningful error message
            println("error registering user: $e")
            Result.failure(Exception("Registration failed: ${e.message}", e))
        }
    }

    override suspend fun signInWithGoogle(context: Context): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Create credential manager and prepare for Google sign-in
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

                // Check if the result contains the credential data
                if (result.credential.data.isEmpty == true) {
                    return@withContext Result.failure(Exception("No credential data found"))
                }

                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = credential.idToken

                // Sign in with Google ID token
                auth.signInWith(IDToken) {
                    this.idToken = idToken
                    provider = Google
                    nonce = rawNonce
                }

                val currentUser = auth.currentUserOrNull()

                // Ensure currentUser is not null
                if (currentUser == null) {
                    return@withContext Result.failure(Exception("Failed to get current user after sign-in"))
                }

                // Prepare user object
                val user = User(
                    id = currentUser.id,
                    email = currentUser.email ?: "",
                    createdAt = currentUser.createdAt.toString(),
                    lastSignedIn = currentUser.lastSignInAt.toString(),
                    avatarId = "",  // You can update this with the user's avatar if available
                    displayName = "",  // You can update this with the user's display name if available
                    bio = ""  // You can update this with the user's bio if available
                )

                // Check if user already exists in the Supabase table
                val existingUser = try {
                    supcli.from("users").select {
                        filter {
                            User::id eq currentUser.id
                        }
                    }.decodeSingleOrNull<User>()
                } catch (e: Exception) {
                    println("Error fetching user from Supabase: $e")
                    null
                }

                if (existingUser != null) {
                    // User already exists, return existing user
                    return@withContext Result.success(existingUser)
                }

                // User does not exist, insert new record
                val actualUser = try {
                    supcli.from("users").insert(user) { select() }.decodeSingle<User>()
                } catch (e: Exception) {
                    println("Error inserting user into Supabase: $e")
                    return@withContext Result.failure(e)
                }

                Result.success(actualUser)

            } catch (e: Exception) {
                // Log and return the error
                println("Error signing in with Google: $e")
                return@withContext Result.failure(e)
            }
        }
    }


    override suspend fun getCurrentUser(): User? {
        val currentUserId = auth.currentUserOrNull()?.id ?: return null

        val query = supcli.from("users").select {
            filter {
                User::id eq currentUserId
            }
        }.decodeSingleOrNull<User>()

        return query?.let {
            val data = User(
                id = it.id,
                email = it.email,
                avatarId = it.avatarId,
                createdAt = it.createdAt,
                lastSignedIn = it.lastSignedIn,
                displayName = it.displayName,
                bio = it.bio
            )
            println("user data: $data")
            data
        }
    }

    override suspend fun signOutUser(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
