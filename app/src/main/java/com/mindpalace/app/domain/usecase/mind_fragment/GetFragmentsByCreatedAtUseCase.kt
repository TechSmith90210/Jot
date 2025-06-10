package com.mindpalace.app.domain.usecase.mind_fragment

import com.mindpalace.app.domain.model.MindFragmentSummary
import com.mindpalace.app.domain.repository.MindFragmentRepository

class GetFragmentsByCreatedAtUseCase(private val repository: MindFragmentRepository) {
    suspend operator fun invoke(userId: String, limit: Long = 7): Result<List<MindFragmentSummary>> {
        return repository.getFragmentsByCreatedAt(userId, limit)
    }
}