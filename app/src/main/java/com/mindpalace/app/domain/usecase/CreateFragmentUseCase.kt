package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.repository.MindFragmentRepository
import com.mindpalace.app.domain.model.MindFragment

class CreateFragmentUseCase(private val repository : MindFragmentRepository) {
    suspend operator fun invoke(fragment: MindFragment) {
       repository.createFragment(fragment)
    }
}