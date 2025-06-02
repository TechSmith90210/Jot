package com.mindpalace.app.domain.usecase.mind_blog

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.repository.MindBlogRepository

class GetUserBlogUseCase (private val repository: MindBlogRepository) {
    suspend operator fun invoke(userId:String) : Result<List<MindBlog>> {
        return repository.getUserBlogs(userId)
    }
}