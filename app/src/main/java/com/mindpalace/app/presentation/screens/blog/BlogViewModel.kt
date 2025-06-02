package com.mindpalace.app.presentation.screens.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.usecase.mind_blog.MindBlogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val mindBlogUseCases: MindBlogUseCases
) : ViewModel() {
    private val _state = MutableStateFlow<BlogState>(BlogState.Idle)
    val state: StateFlow<BlogState> = _state

    private val _blog = MutableStateFlow<MindBlog?>(null)
    val blog: StateFlow<MindBlog?> = _blog.asStateFlow()

    fun createMindBlog() {
        val title = ""
        val description = ""
        val defaultContent = """
        {
          "title": "",
          "blocks": [
            {
              "id": "${UUID.randomUUID()}",
              "text": ""
            }
          ]
        }
    """.trimIndent()

        viewModelScope.launch {
            _state.value = BlogState.Loading
            val result = mindBlogUseCases.createBlogUseCase(
                title = title,
                description = description,
                content = defaultContent
            )
            _state.value = if (result.isSuccess) {
                val blogId = result.getOrNull() ?: ""
                println("Blog creation result: $result")
                println("Blog creation result: $blogId")


                BlogState.Success(
                    blogId = blogId,
                )
            } else {
                BlogState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun resetState() {
        _state.value = BlogState.Idle
    }

    fun getAllBlogs() {
        viewModelScope.launch {
            _state.value = BlogState.Loading
            val result = mindBlogUseCases.getAllBlogsUseCase()
            if (result.isSuccess) {
                _state.value = BlogState.SuccessList(allBlogs = result.getOrNull() ?: emptyList())
            } else {
                _state.value = BlogState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun getLatestBlogs() {
        viewModelScope.launch {
            _state.value = BlogState.Loading
            val result = mindBlogUseCases.getLatestBlogsUseCase()
            if (result.isSuccess) {
                _state.value =
                    BlogState.SuccessList(latestBlogs = result.getOrNull() ?: emptyList())
            } else {
                _state.value = BlogState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun getUserBlogs(id: String) {
        viewModelScope.launch {
            _state.value = BlogState.Loading
            val result = mindBlogUseCases.getUserBlogUseCase(id)
            if (result.isSuccess) {
                _state.value = BlogState.SuccessList(userBlogs = result.getOrNull() ?: emptyList())
            } else {
                _state.value = BlogState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun getBlogById(id: String) {
        viewModelScope.launch {
            _state.value = BlogState.Loading

            val result = mindBlogUseCases.getBlogByIdUseCase(id)
            result.fold(
                onSuccess = { blog ->
                    if (true) {
                        _state.value = BlogState.SuccessBlog(blog)
                        _blog.value = blog
                    } else {
                        _state.value = BlogState.Error("Blog not found.")
                    }
                },
                onFailure = { exception ->
                    _state.value = BlogState.Error(exception.message ?: "Unknown error occurred.")
                }
            )
        }
    }

    fun updateBlog(blog: MindBlog) {
        viewModelScope.launch {
            _state.value = BlogState.Loading
            val result = mindBlogUseCases.updateBlogUseCase(blog)
            _state.value = if (result.isSuccess) {
                BlogState.Success(blogId = blog.id)
            } else {
                BlogState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun deleteBlog(id: String) {
        viewModelScope.launch {
            _state.value = BlogState.Loading
            val result = mindBlogUseCases.deleteBlogUseCase(id)
            _state.value = if (result.isSuccess) {
                BlogState.Success(blogId = id)
            } else {
                BlogState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

}