package com.mindpalace.app.presentation.screens.auth.login

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AppTextField
import com.mindpalace.app.presentation.components.LoadingScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(modifier: Modifier, navController: NavController, viewModel: LoginViewModel, onNavigateToHome : () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Return Pair<isValid, errorMessage?>
    fun validateFields(email: String, password: String): Pair<Boolean, String?> {
        return when {
            email.isBlank() -> Pair(false, "Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(false, "Invalid email format")
            password.isBlank() -> Pair(false, "Password cannot be empty")
            else -> Pair(true, null)
        }
    }

    fun onLoginClick() {
        val (isValid, errorMessage) = validateFields(email.value, password.value)
        if (isValid) {
            viewModel.login(email.value, password.value)
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMessage ?: "Unknown error")
            }
        }
    }


    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                onNavigateToHome()
            }
            is LoginState.Error -> {
                snackbarHostState.showSnackbar((loginState as LoginState.Error).message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = { /* Empty */ },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left_line),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            if (loginState is LoginState.Loading) {
                LoadingScreen()
            }

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(R.drawable.login_image),
                    contentDescription = "Login Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 230.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(25.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Welcome Back!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Continue Building Your MindPalace.",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(25.dp))

                        AppTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AppTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = "Password",
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = { onLoginClick() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = loginState !is LoginState.Loading
                        ) {
                            Text("Login", style = MaterialTheme.typography.labelLarge)
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        OutlinedButton(
                            onClick = { /* TODO: Google Sign In */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google_icon_logo),
                                contentDescription = "Google Sign-In",
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(end = 12.dp),
                                tint = Color.Unspecified
                            )
                            Text("Continue with Google", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    )
}
