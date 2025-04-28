package com.mindpalace.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state : StateFlow<ProfileState> = _state

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading

            try{
                val user = getCurrentUserUseCase.invoke()
                if(user!=null){
                    _state.value = ProfileState.Success(user)
                }else{
                    _state.value = ProfileState.Error("User not found")
                }
            }catch(e: Exception){
                _state.value = ProfileState.Error(e.message ?: "Unknown error")
            }
        }
    }
}