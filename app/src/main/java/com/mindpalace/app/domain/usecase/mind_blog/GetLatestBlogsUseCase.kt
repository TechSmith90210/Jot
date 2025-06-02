package com.mindpalace.app.domain.usecase.mind_blog

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.model.MindBlogWithUser
import com.mindpalace.app.domain.repository.MindBlogRepository

class GetLatestBlogsUseCase (private val repository: MindBlogRepository) {
    suspend operator fun invoke () : Result<List<MindBlogWithUser>> {
        return repository.getLatestBlogs()
    }
}