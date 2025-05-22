package com.mindpalace.app.presentation.screens.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.presentation.components.BottomNavBar
import com.mindpalace.app.presentation.screens.blog.BlogScreen
import com.mindpalace.app.presentation.screens.home.HomeScreen
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentEditorScreen
import com.mindpalace.app.presentation.screens.profile.ProfileScreen
import com.mindpalace.app.presentation.screens.profile.ProfileViewModel
import com.mindpalace.app.presentation.screens.search.SearchScreen
import com.mindpalace.app.presentation.screens.settings.SettingsScreen
import java.util.UUID

@Composable
fun RootScreen() {
    val bottomNavController = rememberNavController()

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        "home_screen",
        "search_screen",
        "blogs_screen",
        "profile_screen"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = bottomNavController, onCenterButtonClick = {
                    val randomId = UUID.randomUUID().toString()
                    bottomNavController.navigate("mind_fragment_editor/$randomId")
                })
            }
        },
        content = { paddingValues ->
            NavHost(
                navController = bottomNavController,
                startDestination = "home_screen",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home_screen") { HomeScreen() }
                composable("search_screen") { SearchScreen(Modifier) }
                composable("blogs_screen") { BlogScreen(Modifier) }
                composable("profile_screen") {
                    val viewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        onNavigateToSettings = { bottomNavController.navigate("settings_screen") },
                        profileViewModel = viewModel
                    )
                }
                composable("mind_fragment_editor/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    if (id != null) {
                        MindFragmentEditorScreen(id = id, onNavigateBack = { bottomNavController.popBackStack() })
                    } else {
                        bottomNavController.popBackStack()
                    }
                }
                composable("settings_screen") {
                    SettingsScreen(
                        Modifier,
                        onNavigateBack = { bottomNavController.navigate("profile_screen") },
                        onSignOutSuccess = {

                        }
                    )
                }
            }
        }
    )
}

