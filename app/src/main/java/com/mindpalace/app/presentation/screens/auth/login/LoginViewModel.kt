package com.mindpalace.app.presentation.screens.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.domain.usecase.GoogleSignInUseCase
import com.mindpalace.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val response = loginUseCase.execute(email, password)

            response.fold(
                onSuccess = {
                    _loginState.value = LoginState.Success
                },
                onFailure = { error ->
                    _loginState.value = LoginState.Error(message = error.message ?: "Unknown error")
                })
        }
    }

    @OptIn(ExperimentalTime::class)
    fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = googleSignInUseCase.invoke(context)

            result.fold(
                onSuccess = { user ->
                    val createdAt = user.createdAt
                    val lastSignInAt = user.lastSignedIn

                    val isFirstLogin = kotlin.runCatching {
                        val created = Instant.parse(createdAt).epochSeconds
                        val lastSignIn = Instant.parse(lastSignInAt).epochSeconds
                        (lastSignIn - created) <= 5
                    }.getOrDefault(false)

                    if (isFirstLogin) {
                        _loginState.value = LoginState.OnBoard
                    } else {
                        _loginState.value = LoginState.Success
                    }
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: "An unknown error occurred"
                    _loginState.value = LoginState.Error(errorMessage)
                }
            )
        }
    }


}