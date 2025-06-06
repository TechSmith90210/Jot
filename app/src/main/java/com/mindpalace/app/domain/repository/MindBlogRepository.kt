package com.mindpalace.app.domain.repository

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.model.MindBlogWithUser

interface MindBlogRepository {
    //Create
    suspend fun createBlog(
        title: String, description: String, content: String = """
               {
               "title": "",
               "blocks" : []
               }
            """.trimIndent()
    ): Result<String>

    //Read
    suspend fun getAllBlogs(): Result<List<MindBlogWithUser>>
    suspend fun getLatestBlogs(): Result<List<MindBlogWithUser>>

    suspend fun getUserBlogs(id: String): Result<List<MindBlog>>
    suspend fun getBlogById(id: String): Result<MindBlogWithUser>

    //Update
    suspend fun updateBlog(blog: MindBlog): Result<Unit>

    //Delete
    suspend fun deleteBlog(id: String): Result<Unit>
}