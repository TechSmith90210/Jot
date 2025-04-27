package com.mindpalace.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mindpalace.app.presentation.screens.auth.login.LoginScreen
import com.mindpalace.app.presentation.screens.auth.login.LoginViewModel
import com.mindpalace.app.presentation.screens.auth.signUp.SignUpScreen
import com.mindpalace.app.presentation.screens.auth.signUp.SignupViewModel
import com.mindpalace.app.presentation.screens.home.HomeScreen
import com.mindpalace.app.presentation.screens.onboarding.WelcomeScreen
import com.mindpalace.app.presentation.screens.onboarding.avatarSelection.AvatarSelectionViewModel
import com.mindpalace.app.presentation.screens.onboarding.avatarSelection.AvatarSelectorScreen
import com.mindpalace.app.presentation.screens.onboarding.splash.SplashScreen
import com.mindpalace.app.presentation.screens.onboarding.splash.SplashViewModel
import com.mindpalace.app.presentation.screens.root.RootScreen

@Composable
fun MindNavigator(
    navController: NavHostController, modifier: Modifier = Modifier
) {

    NavHost(navController = navController, startDestination = "splashScreen") {
        // splash screen nav
        composable("splashScreen") {
            val splashViewModel: SplashViewModel = hiltViewModel()

            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("welcomeScreen") {
                        popUpTo("splashScreen") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                    navController.navigate("rootScreen") {
                        popUpTo("splashScreen") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                viewModel = splashViewModel
            )
        }

        //onboarding screens
        composable("welcomeScreen") {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate("createAccountScreen") },
                onLoginClick = { navController.navigate("loginScreen") },
                modifier = modifier
            )
        }
        composable("createAccountScreen") {
            val signupViewModel: SignupViewModel = hiltViewModel()
            SignUpScreen(
                modifier = modifier,
                navController = navController,
                viewModel = signupViewModel
            )
        }
        composable("loginScreen") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                modifier = modifier,
                navController = navController,
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate("rootScreen") {
                        popUpTo("loginScreen") { inclusive = true }
                    }
                },
                onNavigateToOnboarding =  {
                    navController.navigate("avatarSelectorScreen") {
                        popUpTo("loginScreen") { inclusive = true }
                    }
                }
            )
        }
        composable("avatarSelectorScreen") {
            val avatarSelectionViewModel: AvatarSelectionViewModel = hiltViewModel()
            AvatarSelectorScreen(
                modifier = modifier,
                navController = navController,
                viewModel = avatarSelectionViewModel
            )
        }

        // main screens
        composable("rootScreen") {
            RootScreen(modifier = modifier)
        }
        // ADD THIS
        composable("homeScreen") {
            HomeScreen()
        }
    }
}
