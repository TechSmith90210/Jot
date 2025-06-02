package com.mindpalace.app.domain.usecase.mind_blog

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.repository.MindBlogRepository

class GetBlogByIdUseCase (private val repository: MindBlogRepository) {
    suspend operator fun invoke(id: String) : Result<MindBlog> {
        return repository.getBlogById(id)
    }
}