package com.mindpalace.app.data.repository

import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.model.MindFragment
import com.mindpalace.app.domain.model.MindFragmentSummary
import com.mindpalace.app.domain.repository.MindFragmentRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import java.time.Instant
import java.util.UUID

class MindFragmentRepositoryImpl(supabaseClient: SupabaseClient) : MindFragmentRepository {

    private val auth: Auth = supabaseClient.client.auth
    private val supcli = supabaseClient.client
    private val now = Instant.now().toString()

    override suspend fun createFragment(): Result<String> {
        return try {
            val title = ""
            val defaultContent = """
        {
          "title": "",
          "blocks": [
            {
              "id": "${UUID.randomUUID()}",
              "text": ""
            }
          ]
        }
    """.trimIndent()
            val currentUserId =
                auth.currentUserOrNull()?.id ?: return Result.failure(Exception("User is null"))
            val fragmentToInsert = MindFragment(
                id = UUID.randomUUID().toString(),
                userId = currentUserId,
                createdAt = now,
                lastOpenedAt = now,
                lastUpdatedAt = now,
                title = title,
                content = defaultContent
            )

            // This will throw if it fails — so if no exception, it's successful
            supcli.from("mind_fragments").insert(fragmentToInsert)

            Result.success(fragmentToInsert.id)
        } catch (e: Exception) {
            println("Error creating fragment: $e")
            Result.failure(e)
        }
    }

    // this gets the most recently opened fragments, used for the recent fragments carousel in home page
    override suspend fun getFragmentsByLastOpened(
        userId: String, limit: Long
    ): Result<List<MindFragmentSummary>> {
        return try {
            val currentUserId =
                auth.currentUserOrNull()?.id ?: return Result.failure(Exception("User is null"))
            val response = supcli.from("mind_fragments").select {
                filter {
                    eq("user_id", currentUserId)
                }
                order(column = "last_opened_at", order = Order.DESCENDING)
                limit(count = limit)
            }.decodeAs<List<MindFragmentSummary>>()
            Result.success(response)
        } catch (e: Exception) {
            println("Error getting fragments by last opened : $e")
            Result.failure(e)
        }
    }

    override suspend fun getFragmentsByCreatedAt(
        userId: String, limit: Long
    ): Result<List<MindFragmentSummary>> {
        return try {
            val currentUserId =
                auth.currentUserOrNull()?.id ?: return Result.failure(Exception("User is null"))
            val response = supcli.from("mind_fragments").select {
                filter {
                    eq("user_id", currentUserId)
                }
                order(column = "created_at", order = Order.DESCENDING)
                limit(count = limit)
            }.decodeAs<List<MindFragmentSummary>>()
            Result.success(response)
        } catch (e: Exception) {
            println("Error getting fragments by last opened : $e")
            Result.failure(e)
        }
    }

    override suspend fun getAllFragments(userId: String): Result<List<MindFragmentSummary>> {
        return try {
            val response = supcli.from("mind_fragments").select {
                filter {
                    eq("user_id", userId)
                }
                order(column = "created_at", order = Order.DESCENDING)
            }.decodeAs<List<MindFragmentSummary>>()
            Result.success(response)
        } catch (e: Exception) {
            println("Error getting fragments by last opened : $e")
            Result.failure(e)
        }
    }

    override suspend fun getFragment(
        fragmentId: String
    ): Result<MindFragment> {
        val currentUserId =
            auth.currentUserOrNull()?.id ?: return Result.failure(Exception("User is null"))
        return try {
            val response = supcli.from("mind_fragments").select {
                filter {
                    eq("user_id", currentUserId)
                    eq("id", fragmentId)
                }
                limit(count = 1)
            }.decodeSingle<MindFragment>()

            //updating the last_opened_at of the fragment when the fragment is accessed
            try {
                supcli.from("mind_fragments").update(
                    {
                        set("last_opened_at", now)
                    }
                )
                {
                    filter {
                        eq("user_id", currentUserId)
                        eq("id", fragmentId)
                    }
                }
            } catch (e: Exception) {
                println("Failed to update last_opened_at: $e") // Silent fail
            }

            println("here u go sir: $response")
            Result.success(response)
        } catch (e: Exception) {
            println("Error getting fragments by last opened : $e")
            Result.failure(e)
        }
    }

    override suspend fun updateFragment(fragment: MindFragment): Result<Unit> {
        return try {
            val updatedFragment = fragment.copy(
                lastUpdatedAt = Instant.now().toString(),
                lastOpenedAt = Instant.now().toString()
            )

            supcli.from("mind_fragments")
                .update(updatedFragment) {
                    filter {
                        eq("id", updatedFragment.id)
                        eq("user_id", updatedFragment.userId)
                    }
                }.decodeAs<List<MindFragment>>()

            Result.success(Unit)

        } catch (e: Exception) {
            println("Error updating fragment: $e")
            Result.failure(e)
        }
    }


    override suspend fun deleteFragment(
        userId: String,
        fragmentId: String
    ): Result<Unit> {
        return try {
            supcli.from("mind_fragments").delete {
                filter {
                    eq("user_id", userId)
                    eq("id", fragmentId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error deleting fragment: $e")
            Result.failure(e)
        }
    }

}