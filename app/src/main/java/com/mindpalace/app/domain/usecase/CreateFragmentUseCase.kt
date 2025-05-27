package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.repository.MindFragmentRepository
import com.mindpalace.app.domain.model.MindFragment

class CreateFragmentUseCase(private val repository: MindFragmentRepository) {
    suspend operator fun invoke(title: String, content: String): Result<String> {
        return repository.createFragment(title = title, content = content)
    }
}