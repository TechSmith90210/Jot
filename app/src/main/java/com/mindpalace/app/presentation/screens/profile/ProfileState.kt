package com.mindpalace.app.presentation.screens.profile

import com.mindpalace.app.domain.model.User

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val profile: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}