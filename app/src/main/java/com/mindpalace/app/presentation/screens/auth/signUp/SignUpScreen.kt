package com.mindpalace.app.presentation.screens.auth.signUp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AppTextField
import com.mindpalace.app.presentation.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val signUpState by viewModel.signupState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    fun onSignUpClick(email: String, password: String, confirmPassword: String) {
        when {
            email.isEmpty() -> {
                errorMessage = "Please enter your email address."
            }

            password.isEmpty() -> {
                errorMessage = "Please enter your password."
            }

            confirmPassword.isEmpty() -> {
                errorMessage = "Please confirm your password."
            }

            password != confirmPassword -> {
                errorMessage = "Passwords do not match."
            }

            else -> {
                viewModel.signUp(email, password)
                errorMessage = ""
            }
        }
    }

    // Handle sign-up states
    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignupState.Success -> {
                navController.navigate("avatarSelectorScreen") {
                    popUpTo("signUpScreen") { inclusive = true }
                }
            }

            is SignupState.Error -> {
                snackbarHostState.showSnackbar((signUpState as SignupState.Error).message)
            }

            else -> Unit
        }
    }
    val context = LocalContext.current

    fun onGoogleSignInClick() {
       viewModel.signInWithGoogle(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {},
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        if (signUpState is SignupState.Loading) {
            LoadingScreen()
        } else {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(R.drawable.register_image),
                    contentDescription = "Create Account Image",
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
                            "Create Account",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Enter details and begin your MindPalace journey!",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(25.dp))

                        AppTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        AppTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        AppTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Confirm Password",
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = { onSignUpClick(email, password, confirmPassword) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sign Up", style = MaterialTheme.typography.labelLarge)
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        OutlinedButton(
                            onClick = { onGoogleSignInClick() },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                                contentColor = if (isSystemInDarkTheme()) Color.White else Color(
                                    0xff3C4043
                                )
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(
                                1.dp,
                                if (isSystemInDarkTheme()) Color(0xFF5F6368) else Color(0xFFDADCE0)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google_icon_logo),
                                contentDescription = "Google Sign-In",
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(end = 12.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                "Continue with Google",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    LaunchedEffect(errorMessage) {
                        snackbarHostState.showSnackbar(errorMessage)
                        errorMessage = ""
                    }
                }
            }
        }
    }
}
