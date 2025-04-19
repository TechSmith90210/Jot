package com.mindpalace.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mindpalace.app.presentation.HomeScreen
import com.mindpalace.app.presentation.auth.login.LoginScreen
import com.mindpalace.app.presentation.auth.login.LoginViewModel
import com.mindpalace.app.presentation.auth.signUp.SignUpScreen
import com.mindpalace.app.presentation.auth.signUp.SignupViewModel
import com.mindpalace.app.presentation.onboarding.SplashScreen
import com.mindpalace.app.presentation.onboarding.WelcomeScreen
import com.mindpalace.app.presentation.onboarding.avatarSelection.AvatarSelectionViewModel
import com.mindpalace.app.presentation.onboarding.avatarSelection.AvatarSelectorScreen

@Composable
fun MindNavigator(
    navController: NavHostController, modifier: Modifier = Modifier
) {

    NavHost(navController = navController, startDestination = "splashScreen") {

        // splash screen nav
        composable("splashScreen") {
            SplashScreen(navController)
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
            SignUpScreen(modifier = modifier, navController = navController, viewModel = signupViewModel)
        }
        composable("loginScreen") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                modifier = modifier,
                navController = navController,
                viewModel = loginViewModel)
        }
        composable("avatarSelectorScreen"){
        val avatarSelectionViewModel : AvatarSelectionViewModel = hiltViewModel()
            AvatarSelectorScreen(modifier = modifier, navController = navController, viewModel = avatarSelectionViewModel)
        }

        // main screens
        composable("homeScreen") {
            HomeScreen(modifier = modifier)
        }
    }
}
