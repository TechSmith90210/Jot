package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.model.MindFragmentSummary
import com.mindpalace.app.domain.repository.MindFragmentRepository

class GetFragmentsByLastOpenedUseCase (private val repository: MindFragmentRepository) {
    suspend operator fun invoke(userId: String, limit: Long = 6): Result<List<MindFragmentSummary>> {
        return repository.getFragmentsByLastOpened(userId, limit)
    }
}