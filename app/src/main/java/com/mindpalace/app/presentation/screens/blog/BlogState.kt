package com.mindpalace.app.presentation.screens.blog

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.model.MindBlogWithUser

sealed class BlogState {
    object Idle : BlogState()
    object Loading : BlogState()

    data class Success(val blogId: String? = null) : BlogState()

    data class SuccessBlog(val blog: MindBlogWithUser? = null) : BlogState()

    data class SuccessList(
        val latestBlogs: List<MindBlogWithUser>? = emptyList(),
        val allBlogs: List<MindBlogWithUser>? = emptyList(),
        val userBlogs: List<MindBlog>? = emptyList()
    ) : BlogState()

    data class Error(val message: String) : BlogState()
}