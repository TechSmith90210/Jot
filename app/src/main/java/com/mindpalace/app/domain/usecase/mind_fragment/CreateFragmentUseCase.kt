package com.mindpalace.app.domain.usecase.mind_fragment

import com.mindpalace.app.domain.repository.MindFragmentRepository

class CreateFragmentUseCase(private val repository: MindFragmentRepository) {
    suspend operator fun invoke(title: String, content: String): Result<String> {
        return repository.createFragment(title = title, content = content)
    }
}