package com.mindpalace.app.data.repository

import android.util.Log
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.model.MindBlogWithUser
import com.mindpalace.app.domain.repository.MindBlogRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import java.time.Instant
import java.util.UUID

class MindBlogRepositoryImpl(
    supabaseClient: SupabaseClient
) : MindBlogRepository {

    private val auth: Auth = supabaseClient.client.auth
    private val blogs = supabaseClient.client.from("mind_blogs")
    private val now = Instant.now().toString()

    override suspend fun createBlog(
        title: String,
        description: String,
        content: String,
    ): Result<String> {
        return try {
            val currentUserId =
                auth.currentUserOrNull()?.id ?: return Result.failure(Exception("User is null"))

            val blog = MindBlog(
                id = UUID.randomUUID().toString(),
                authorId = currentUserId,
                title = title,
                description = description,
                content = content,
                publishDate = now,
                lastUpdated = now,
            )

            blogs.insert(blog)
            Result.success(blog.id)
        } catch (e: Exception) {
            println("Error creating blog: $e")
            Result.failure(e)
        }
    }

    override suspend fun getAllBlogs(): Result<List<MindBlogWithUser>> {
        return try {
            val allBlogs = blogs.select(
                columns = Columns.raw(
                    """
        *,
        user:author_id(display_name, avatar_id)
        """.trimIndent()
                )
            ) {
                order("publish_date", Order.DESCENDING)
            }.decodeList<MindBlogWithUser>().shuffled()
//            Log.d(
//                "MindBlogRepositoryImpl", "getAllBlogs: $allBlogs"
//            )
            Result.success(allBlogs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLatestBlogs(): Result<List<MindBlogWithUser>> {
        return try {
            val latestBlogs = blogs.select(
                columns = Columns.raw(
                    """
                           *,
                            user:author_id(display_name, avatar_id)
                    """.trimIndent()
                )
            ) {
                order("publish_date", Order.DESCENDING)
                limit(5)
            }.decodeList<MindBlogWithUser>()
            Log.d(
                "MindBlogRepositoryImpl", "getLatestBlogs: $latestBlogs"
            )
            Result.success(latestBlogs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserBlogs(id: String): Result<List<MindBlog>> {
        return try {
            val userBlogs = blogs.select {
                filter { eq("author_id", id) }
            }.decodeList<MindBlog>()
            Result.success(userBlogs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBlogById(id: String): Result<MindBlogWithUser> {
        return try {
            val blog = blogs.select(
                columns = Columns.raw(
                    """
                        *,
                        user:author_id(display_name,avatar_id)
                    """.trimIndent()
                )
            ) {
                filter { eq("id", id) }
            }.decodeSingle<MindBlogWithUser>()
            Result.success(blog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBlog(blog: MindBlog): Result<Unit> {
        return try {
            val updatedBlog = blog.copy(lastUpdated = Instant.now().toString())
            blogs.update(updatedBlog) {
                filter {
                    eq("id", blog.id)
                    eq("author_id", blog.authorId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBlog(id: String): Result<Unit> {
        return try {
            blogs.delete {
                filter { eq("id", id) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}