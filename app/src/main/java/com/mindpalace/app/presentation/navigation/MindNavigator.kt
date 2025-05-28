package com.mindpalace.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mindpalace.app.presentation.screens.all_fragments.AllFragmentsScreen
import com.mindpalace.app.presentation.screens.auth.login.LoginScreen
import com.mindpalace.app.presentation.screens.auth.login.LoginViewModel
import com.mindpalace.app.presentation.screens.auth.signUp.SignUpScreen
import com.mindpalace.app.presentation.screens.auth.signUp.SignupViewModel
import com.mindpalace.app.presentation.screens.blog.BlogScreen
import com.mindpalace.app.presentation.screens.home.HomeScreen
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentEditorScreen
import com.mindpalace.app.presentation.screens.onboarding.WelcomeScreen
import com.mindpalace.app.presentation.screens.onboarding.avatarSelection.AvatarSelectionViewModel
import com.mindpalace.app.presentation.screens.onboarding.avatarSelection.AvatarSelectorScreen
import com.mindpalace.app.presentation.screens.onboarding.splash.SplashScreen
import com.mindpalace.app.presentation.screens.onboarding.splash.SplashViewModel
import com.mindpalace.app.presentation.screens.profile.ProfileScreen
import com.mindpalace.app.presentation.screens.profile.ProfileViewModel
import com.mindpalace.app.presentation.screens.root.RootScreen
import com.mindpalace.app.presentation.screens.search.SearchScreen
import com.mindpalace.app.presentation.screens.settings.SettingsScreen

@Composable
fun MindNavigator(
    navController: NavHostController, modifier: Modifier = Modifier
) {

    NavHost(navController = navController, startDestination = "splashScreen") {
        // splash screen nav
        composable("splashScreen") {
            val splashViewModel: SplashViewModel = hiltViewModel()

            SplashScreen(
                onNavigateToWelcome = {
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
                onNavigateToOnboarding = {
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
        // Nested root screen graph
        navigation(
            startDestination = "home_screen",
            route = "rootGraph"
        ) {
            // RootScreen that contains the Scaffold and BottomNavBar
            composable("rootScreen") {
                RootScreen()
            }

            composable("home_screen") {
                HomeScreen(
                    onFragmentClick = { id ->
                        navController.navigate("mind_fragment_editor/$id")
                    },
                    onCreateFragmentClick = {
                        navController.navigate("mind_fragment_editor")
                    },
                    onViewMoreClick = {
                        navController.navigate("all_fragments_screen")
                    }
                )
            }
            composable("all_fragments_screen") {
                AllFragmentsScreen(
                    onNavigateBack = { navController.navigate("home_screen") },
                    onFragmentClick = { id ->
                        navController.navigate("mind_fragment_editor/$id")
                    },
                    onCreateFragmentClick = {}
                )

            }
            composable("search_screen") { SearchScreen(Modifier) }
            composable("blogs_screen") { BlogScreen(Modifier) }
            composable("profile_screen") {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate("settings_screen") },
                    profileViewModel = viewModel
                )
            }
            composable("mind_fragment_editor/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (id != null) {
                    MindFragmentEditorScreen(
                        id = id,
                        onNavigateBack = { navController.popBackStack() })
                } else {
                    navController.popBackStack()
                }
            }
            composable("settings_screen") {
                SettingsScreen(
                    Modifier,
                    onNavigateBack = { navController.navigate("profile_screen") },
                    onSignOutSuccess = { navController.navigate("splashScreen") }
                )
            }
        }

    }
}
