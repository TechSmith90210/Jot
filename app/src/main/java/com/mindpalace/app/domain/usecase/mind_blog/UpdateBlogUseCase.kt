package com.mindpalace.app.domain.usecase.mind_blog

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.repository.MindBlogRepository

class UpdateBlogUseCase(private val repository: MindBlogRepository) {
    suspend operator fun invoke(blog: MindBlog): Result<Unit> {
        return repository.updateBlog(blog)
    }
}