@file:Suppress("DEPRECATION")

package com.mindpalace.app.presentation.screens.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.mindpalace.app.core.SupabaseClient
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
import com.mindpalace.app.presentation.screens.profile.edit_profile.EditProfileScreen
import com.mindpalace.app.presentation.screens.search.SearchScreen
import io.github.jan.supabase.auth.auth

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootScreen(
    onNavigateToSplashScreen: () -> Unit
) {

    val mindFragmentViewModel: MindFragmentViewModel = hiltViewModel()
    val state by mindFragmentViewModel.state.collectAsState()
    val bottomNavController = rememberNavController()
    val currentUserId = SupabaseClient.client.auth.currentUserOrNull()?.id

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

    val showBottomBar = currentRoute?.startsWith("home_screen") == true ||
            currentRoute?.startsWith("search_screen") == true ||
            currentRoute?.startsWith("blogs_screen") == true ||
            currentRoute?.startsWith("profile_screen/") == true


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = bottomNavController,
                    currentUserId = currentUserId ?: ""
                )
            }
        },
        content = {
            AnimatedNavHost(
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
                composable("profile_screen/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")

                    val viewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        userId = id,
                        profileViewModel = viewModel,
                        onClickUserBlog = { id ->
                            bottomNavController.navigate("mind_blog_editor/$id")
                        },
                        onSignOut = {
                            onNavigateToSplashScreen()
                        },
                        onCreateBlog = {
                            mindBlogViewModel.createMindBlog()
                        },
                        onClickEditProfile = {
                            bottomNavController.navigate("edit_profile_screen")
                        },
                        onNavigateBack = { bottomNavController.popBackStack() }
                    )
                }
                composable("mind_fragment_editor/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    if (id != null) {
                        MindFragmentEditorScreen(
                            id = id,
                            onNavigateBack = { bottomNavController.popBackStack() })
                    } else {
                        bottomNavController.navigate("profile_screen")
                    }
                }
                composable("mind_blog_editor/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    if (id != null) {
                        MindBlogEditorScreen(
                            blogId = id,
                            onNavigateBack = { bottomNavController.popBackStack() },
                            onClickAuthor = { id ->
                                bottomNavController.navigate("profile_screen/$id")
                            }
                            )
                    } else {
                        bottomNavController.popBackStack()
                    }
                }
//                composable("settings_screen") {
//                    SettingsScreen(
//                        Modifier,
//                        onNavigateBack = { bottomNavController.navigate("profile_screen") },
//                        onSignOutSuccess = {
//
//                        }
//                    )
//                }
                composable(
                    "edit_profile_screen",
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it }) + fadeIn()
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                    },
                    popEnterTransition = {
                        slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                    }
                ) {
                    EditProfileScreen(
                        onBackClick = {
                            bottomNavController.navigate("profile_screen")
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

