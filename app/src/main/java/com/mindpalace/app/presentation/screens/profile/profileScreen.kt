package com.mindpalace.app.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AvatarImage
import com.mindpalace.app.presentation.components.pageContents
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.core.convertTimestampToHumanReadableFormat
import com.mindpalace.app.domain.model.User
import com.mindpalace.app.presentation.components.LoadingScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onNavigateToSettings: () -> Unit, profileViewModel: ProfileViewModel = hiltViewModel()) {

    val state by profileViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.fetchCurrentUser()
    }

    when(state) {
        is ProfileState.Loading -> {
            LoadingScreen()
        }
        is ProfileState.Success -> {

            val user = (state as ProfileState.Success).profile

            Scaffold(topBar = {
                TopAppBar(
                    title = { Text("My Profile", style = MaterialTheme.typography.titleSmall) },
                    actions = {
                        IconButton(onClick = { onNavigateToSettings() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.settings_2_line),
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Search"
                            )
                        }
                    },
                    expandedHeight = 40.dp,
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        scrolledContainerColor = MaterialTheme.colorScheme.background
                    )
                )
            }, content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 0.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
                        .fillMaxHeight(),
                    content = {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.background
                            ), leadingContent = {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        ), contentAlignment = Alignment.Center // ✅ Center the child
                                ) {
                                    AvatarImage(randomSeed = user.avatarId)
                                }
                            }, headlineContent = {
                                Text(
                                    text = user.displayName, style = MaterialTheme.typography.titleSmall,
                                )
                            }, supportingContent = {
                                Column(modifier = Modifier.padding(top = 3.dp)) {
                                    Text(
                                        text = "Joined On ${convertTimestampToHumanReadableFormat(user.createdAt)}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Button(
                                        onClick = { /* TODO */ },
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .fillMaxWidth()
                                            .height(30.dp),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Edit Profile",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            })

                        Text(
                            "My Blogs",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(10.dp)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(pageContents.size) { index ->
                                val page = pageContents[index]
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .aspectRatio(1f)  // Make each item square
                                        .background(
                                            shape = RoundedCornerShape(10.dp),
                                            color = MaterialTheme.colorScheme.surface
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        page.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }

                        }

                    })
            }

            )
        }
        is ProfileState.Error -> {
            val errorMessage = (state as ProfileState.Error).message
            println(errorMessage)
        }
        else -> {}
    }


}