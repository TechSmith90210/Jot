package com.mindpalace.app.domain.usecase.mind_fragment

import com.mindpalace.app.domain.repository.MindFragmentRepository

class DeleteFragmentUseCase (private val repository: MindFragmentRepository) {
    suspend operator fun invoke(userId: String, fragmentId: String): Result<Unit> {
        return repository.deleteFragment(userId, fragmentId)
    }
}