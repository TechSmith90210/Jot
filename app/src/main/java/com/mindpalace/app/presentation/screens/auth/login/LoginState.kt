package com.mindpalace.app.presentation.screens.auth.login

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
    object OnBoard: LoginState()
}