package com.mindpalace.app.domain.usecase.mind_fragment

import com.mindpalace.app.domain.model.MindFragmentSummary
import com.mindpalace.app.domain.repository.MindFragmentRepository

class GetAllFragmentsUseCase(private val repository: MindFragmentRepository) {
    suspend operator fun invoke(userId: String): Result<List<MindFragmentSummary>> {
        return repository.getAllFragments(userId)
    }
}