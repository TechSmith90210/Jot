package com.mindpalace.app.presentation.screens.onboarding.avatarSelection

sealed class AvatarSelectionState {
    object Idle : AvatarSelectionState()
    object Loading : AvatarSelectionState()
    object Success : AvatarSelectionState()
    data class Error(val message: String) : AvatarSelectionState()
}