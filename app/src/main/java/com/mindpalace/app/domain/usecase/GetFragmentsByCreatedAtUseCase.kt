package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.model.MindFragmentSummary
import com.mindpalace.app.domain.repository.MindFragmentRepository

class GetFragmentsByCreatedAtUseCase(private val repository: MindFragmentRepository) {
    suspend operator fun invoke(userId: String, limit: Int = 7): Result<List<MindFragmentSummary>> {
        return repository.getFragmentsByCreatedAt(userId, limit)
    }
}