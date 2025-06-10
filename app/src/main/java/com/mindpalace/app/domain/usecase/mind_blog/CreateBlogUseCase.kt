package com.mindpalace.app.domain.usecase.mind_blog

import com.mindpalace.app.domain.repository.MindBlogRepository

class CreateBlogUseCase(private val repository: MindBlogRepository) {
    suspend operator fun invoke(title: String, content: String, description: String): Result<String> {
        return repository.createBlog(
            title = title,
            content = content,
            description = description
        )
    }
}