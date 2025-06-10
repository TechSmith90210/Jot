package com.mindpalace.app.domain.usecase.mind_fragment

import com.mindpalace.app.domain.repository.MindFragmentRepository

class CreateFragmentUseCase(private val repository: MindFragmentRepository) {
    suspend operator fun invoke(): Result<String> {
        return repository.createFragment()
    }
}