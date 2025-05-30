package com.mindpalace.app.presentation.screens.onboarding.avatarSelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.domain.usecase.user.UpdateUserAvatarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarSelectionViewModel @Inject constructor(
    private val updateUserAvatarUseCase: UpdateUserAvatarUseCase
) : ViewModel() {

    private val _avatarSelectionState = MutableStateFlow<AvatarSelectionState>(AvatarSelectionState.Idle)
    val avatarSelectionState: StateFlow<AvatarSelectionState> = _avatarSelectionState

    fun updateAvatar(avatarId: String) {
        viewModelScope.launch {
            _avatarSelectionState.value = AvatarSelectionState.Loading
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()

                if (user != null) {
                    val result = updateUserAvatarUseCase.execute(avatarId)

                    result.onSuccess { updatedUser ->
                        _avatarSelectionState.value = AvatarSelectionState.Success
                    }.onFailure { error ->
                        _avatarSelectionState.value = AvatarSelectionState.Error(
                            error.message ?: "Unknown error"
                        )
                    }
                } else {
                    _avatarSelectionState.value = AvatarSelectionState.Error("User is not authenticated")
                }
            } catch (e: Exception) {
                _avatarSelectionState.value = AvatarSelectionState.Error(e.message ?: "Unknown error")
            }
        }
    }

}

