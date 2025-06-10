package com.mindpalace.app.domain.usecase.mind_fragment

import com.mindpalace.app.domain.model.MindFragment
import com.mindpalace.app.domain.repository.MindFragmentRepository

class UpdateFragmentUseCase (private val repository: MindFragmentRepository) {
    suspend operator fun invoke (mindFragment: MindFragment): Result<Unit>{
        return repository.updateFragment(mindFragment)
    }
}