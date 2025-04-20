package com.mindpalace.app.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mindpalace.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(navController: NavController, onCenterButtonClick: () -> Unit = {}) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.98f),
        modifier = Modifier
            .height(76.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f),
            )
            .padding(top = 1.dp),

        tonalElevation = 4.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        bottomNavItems.take(2).forEach { item ->
            val selected = currentDestination == item.route

            NavigationBarItem(
                selected = selected, onClick = {
                if (currentDestination != item.route) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                val iconResource = if (selected) item.selectedIcon else item.unselectedIcon
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = item.title,
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
            )
        }

        // Center Plus Button
        FloatingActionButton(
            onClick = onCenterButtonClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .height(35.dp)
                .width(50.dp)
                .padding(horizontal = 7.dp),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_fill), contentDescription = "Add"
            )
        }

        bottomNavItems.drop(2).forEach { item ->
            val selected = currentDestination == item.route

            NavigationBarItem(
                selected = selected, onClick = {
                if (currentDestination != item.route) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                val iconResource = if (selected) item.selectedIcon else item.unselectedIcon
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = item.title,
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
            )
            )
        }
    }
}


val bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        route = "home_screen",
        unselectedIcon = R.drawable.folders_line,
        selectedIcon = R.drawable.folders_fill
    ), BottomNavItem(
        title = "Search",
        route = "search_screen",
        unselectedIcon = R.drawable.search_line,
        selectedIcon = R.drawable.search_fill
    ), BottomNavItem(
        title = "Blogs",
        route = "blogs_screen",
        unselectedIcon = R.drawable.book_shelf_line,
        selectedIcon = R.drawable.book_shelf_fill
    ), BottomNavItem(
        title = "Settings",
        route = "settings_screen",
        unselectedIcon = R.drawable.settings_2_line,
        selectedIcon = R.drawable.settings_2_fill
    )
)

data class BottomNavItem(
    val title: String,
    val route: String,
    val unselectedIcon: Int,
    val selectedIcon: Int,
)
