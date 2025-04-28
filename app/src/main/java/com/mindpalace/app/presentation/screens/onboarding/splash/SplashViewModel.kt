package com.mindpalace.app.presentation.screens.onboarding.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.core.SupabaseClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            SupabaseClient.client.auth.loadFromStorage()

            val session = SupabaseClient.client.auth.currentSessionOrNull()
            _isUserLoggedIn.value = session != null

            Log.d("SplashViewModel", "Session exists: ${session != null}")
        }
    }
}
