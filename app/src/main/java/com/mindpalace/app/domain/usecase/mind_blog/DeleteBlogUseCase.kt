package com.mindpalace.app.domain.usecase.mind_blog

import com.mindpalace.app.domain.repository.MindBlogRepository

class DeleteBlogUseCase(private val repository: MindBlogRepository) {
    suspend operator fun invoke(blogId: String): Result<Unit> {
        return repository.deleteBlog(blogId)
    }
}