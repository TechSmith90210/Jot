package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.model.MindFragment
import com.mindpalace.app.domain.repository.MindFragmentRepository

class GetFragmentUseCase (private val repository: MindFragmentRepository) {
    suspend operator fun invoke(fragmentId : String) : Result<MindFragment> {
       return repository.getFragment( fragmentId = fragmentId)
    }
}