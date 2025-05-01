package com.mindpalace.app.presentation.screens.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.presentation.components.BottomNavBar
import com.mindpalace.app.presentation.screens.blog.BlogScreen
import com.mindpalace.app.presentation.screens.home.HomeScreen
import com.mindpalace.app.presentation.screens.profile.ProfileScreen
import com.mindpalace.app.presentation.screens.profile.ProfileViewModel
import com.mindpalace.app.presentation.screens.search.SearchScreen
import com.mindpalace.app.presentation.screens.settings.SettingsScreen
import java.util.UUID

@Composable
fun RootScreen(modifier: Modifier = Modifier, navController: NavController) {

    val bottomNavController = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavBar(bottomNavController, onCenterButtonClick = {
            val randomId = UUID.randomUUID().toString()
            navController.navigate(
                "mind_fragment_editor/$randomId"
            )
        })
    }, content = { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home_screen",
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable("home_screen") {
                HomeScreen()
            }
            composable("search_screen") {
                SearchScreen(modifier)
            }
            composable("blogs_screen") {
                BlogScreen(modifier)
            }
            composable("settings_screen") {
                SettingsScreen(modifier, onNavigateBack = {
                    bottomNavController.navigate("profile_screen")
                }, onSignOutSuccess = {
                    navController.navigate("splashScreen")
                })
            }
            composable("profile_screen") {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    onNavigateToSettings = { bottomNavController.navigate("settings_screen") },
                    profileViewModel = viewModel
                )
            }
        }
    })
}