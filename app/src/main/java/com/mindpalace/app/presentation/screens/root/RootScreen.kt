package com.mindpalace.app.presentation.screens.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.presentation.components.BottomNavBar
import com.mindpalace.app.presentation.screens.blog.BlogScreen
import com.mindpalace.app.presentation.screens.home.HomeScreen
import com.mindpalace.app.presentation.screens.search.SearchScreen
import com.mindpalace.app.presentation.screens.settings.SettingsScreen

@Composable
fun RootScreen(modifier: Modifier = Modifier) {

    val bottomNavController = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavBar(bottomNavController)
    }, content = { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home_screen",
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
        {
            composable("home_screen") {
                HomeScreen(modifier)
            }
            composable("search_screen") {
                SearchScreen(modifier)
            }
            composable("blogs_screen") {
                BlogScreen(modifier)
            }
            composable("settings_screen") {
                SettingsScreen(modifier)
            }
        }
    })
}