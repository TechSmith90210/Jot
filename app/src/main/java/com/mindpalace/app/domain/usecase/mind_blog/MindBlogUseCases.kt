package com.mindpalace.app.domain.usecase.mind_blog

data class MindBlogUseCases (
    val createBlogUseCase: CreateBlogUseCase,
    val getAllBlogsUseCase: GetAllBlogsUseCase,
    val getBlogByIdUseCase: GetBlogByIdUseCase,
    val getLatestBlogsUseCase: GetLatestBlogsUseCase,
    val getUserBlogUseCase: GetUserBlogUseCase,
    val updateBlogUseCase: UpdateBlogUseCase,
    val deleteBlogUseCase: DeleteBlogUseCase
)