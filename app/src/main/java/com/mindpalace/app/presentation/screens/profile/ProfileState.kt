package com.mindpalace.app.presentation.screens.profile

import com.mindpalace.app.domain.model.MindBlog
import com.mindpalace.app.domain.model.User

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(
        val profile: User,
        val blogs: List<MindBlog>? = null
    ) : ProfileState()
    object SignedOut: ProfileState()
    object ProfileUpdated: ProfileState()

    data class Error(val message: String) : ProfileState()
}