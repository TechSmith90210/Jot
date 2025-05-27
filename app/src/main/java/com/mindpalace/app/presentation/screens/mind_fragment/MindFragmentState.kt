package com.mindpalace.app.presentation.screens.mind_fragment

import com.mindpalace.app.domain.model.MindFragmentSummary

sealed class MindFragmentState{
    object Idle: MindFragmentState()
    object Loading: MindFragmentState()
    data class Success(val fragmentId: String? = null): MindFragmentState()
    data class SuccessList(val summaryList: List<MindFragmentSummary>? = null): MindFragmentState()
    data class Error(val message: String): MindFragmentState()
}