package com.mindpalace.app.presentation.onboarding.avatarSelection

sealed class AvatarSelectionState {
    object Idle : AvatarSelectionState()
    object Loading : AvatarSelectionState()
    object Success : AvatarSelectionState()
    data class Error(val message: String) : AvatarSelectionState()
}