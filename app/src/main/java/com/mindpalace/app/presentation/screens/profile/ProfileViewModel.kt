package com.mindpalace.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.domain.usecase.auth.GetCurrentUserUseCase
import com.mindpalace.app.domain.usecase.auth.SignOutUserUseCase
import com.mindpalace.app.domain.usecase.mind_blog.GetUserBlogUseCase
import com.mindpalace.app.domain.usecase.user.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserBlogUseCase: GetUserBlogUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state


    fun fetchCurrentUser() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading

            try {
                val user = getCurrentUserUseCase.invoke()
                val userBlogs = getUserBlogUseCase.invoke(user?.id ?: "")
                if (user != null) {
                    _state.value = ProfileState.Success(
                        profile = user,
                        blogs = userBlogs.getOrNull() ?: emptyList()
                    )
                } else {
                    _state.value = ProfileState.Error("User not found")
                }
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                signOutUserUseCase.execute()
                _state.value = ProfileState.SignedOut
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateProfile(avatarId: String, displayName: String, bio: String?) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                updateUserProfileUseCase.execute(avatarId, displayName, bio)
                _state.value = ProfileState.ProfileUpdated
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message ?: "Unknown error")
            }
        }
    }
//    fun resetState() {
//        _state.value = ProfileState.Loading
//    }

}