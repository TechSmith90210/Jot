package com.mindpalace.app.presentation.screens.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.presentation.components.BottomNavBar
import com.mindpalace.app.presentation.screens.all_fragments.AllFragmentsScreen
import com.mindpalace.app.presentation.screens.blog.BlogScreen
import com.mindpalace.app.presentation.screens.blog.BlogState
import com.mindpalace.app.presentation.screens.blog.BlogViewModel
import com.mindpalace.app.presentation.screens.blog.MindBlogEditorScreen
import com.mindpalace.app.presentation.screens.home.HomeScreen
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentEditorScreen
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentState
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentViewModel
import com.mindpalace.app.presentation.screens.profile.ProfileScreen
import com.mindpalace.app.presentation.screens.profile.ProfileViewModel
import com.mindpalace.app.presentation.screens.search.SearchScreen
import com.mindpalace.app.presentation.screens.settings.SettingsScreen

@Composable
fun RootScreen() {

    val mindFragmentViewModel: MindFragmentViewModel = hiltViewModel()
    val state by mindFragmentViewModel.state.collectAsState()
    val bottomNavController = rememberNavController()

    LaunchedEffect(state) {
        if (state is MindFragmentState.Success) {
            val fragmentId = (state as MindFragmentState.Success).fragmentId
            bottomNavController.navigate("mind_fragment_editor/$fragmentId")
            mindFragmentViewModel.resetState()
        }
    }

    val mindBlogViewModel: BlogViewModel = hiltViewModel()
    val state1 by mindBlogViewModel.state.collectAsState()

    LaunchedEffect(state1) {
        if (state1 is BlogState.Success) {
            val blogId = (state1 as BlogState.Success).blogId
            bottomNavController.navigate("mind_blog_editor/$blogId")
            mindBlogViewModel.resetState()
        }
    }

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
                BottomNavBar(
                    navController = bottomNavController,)
            }
        },
        content = {
            NavHost(
                navController = bottomNavController,
                startDestination = "home_screen",
                modifier = Modifier.padding(it)
            ) {
                composable("home_screen") {
                    HomeScreen(
                        onFragmentClick = { id ->
                            bottomNavController.navigate("mind_fragment_editor/$id")
                        },
                        onCreateFragmentClick = {
                            mindFragmentViewModel.createFragment()
                        },
                        onViewMoreClick = {
                            bottomNavController.navigate("all_fragments_screen")
                        }
                    )
                }
                composable("search_screen") { SearchScreen(Modifier) }
                composable("blogs_screen") {
                    BlogScreen(
                        Modifier,
                        onCreateBlogClick = {
                            mindBlogViewModel.createMindBlog()
                        },
                        onBlogCLick = { id ->
                            bottomNavController.navigate("mind_blog_editor/$id")
                        }
                    )
                }
                composable("profile_screen") {
                    val viewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        onNavigateToSettings = { bottomNavController.navigate("settings_screen") },
                        profileViewModel = viewModel,
                        onClickUserBlog = { id ->
                            bottomNavController.navigate("mind_blog_editor/$id")
                        }
                    )
                }
                composable("mind_fragment_editor/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    if (id != null) {
                        MindFragmentEditorScreen(
                            id = id,
                            onNavigateBack = { bottomNavController.popBackStack() })
                    } else {
                        bottomNavController.popBackStack()
                    }
                }
                composable("mind_blog_editor/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    if (id != null) {
                        MindBlogEditorScreen(
                            blogId = id,
                            onNavigateBack = { bottomNavController.popBackStack() })
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
                composable("all_fragments_screen") {
                    AllFragmentsScreen(
                        onNavigateBack = { bottomNavController.navigate("home_screen") },
                        onFragmentClick = { id ->
                            bottomNavController.navigate("mind_fragment_editor/$id")
                        },
                        onCreateFragmentClick = {
                            mindFragmentViewModel.createFragment()
                        }
                    )
                }

            }
        }
    )
}

