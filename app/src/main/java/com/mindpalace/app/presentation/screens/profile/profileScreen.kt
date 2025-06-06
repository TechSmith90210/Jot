package com.mindpalace.app.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.core.convertTimestampToHumanReadableFormat
import com.mindpalace.app.core.formatCustomDateTime
import com.mindpalace.app.presentation.components.AvatarImage
import com.mindpalace.app.presentation.components.LoadingScreen
import io.github.jan.supabase.auth.auth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onClickUserBlog: (String) -> Unit,
    onSignOut: () -> Unit,
    onCreateBlog: () -> Unit,
    onClickEditProfile: () -> Unit,
    userId: String? = null,
    onNavigateBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val state by profileViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.getUserProfile(userId.toString())
    }


    fun onConfirmSignOut() {
        profileViewModel.signOut()
    }

    when (state) {
        is ProfileState.Loading -> LoadingScreen()
        is ProfileState.SignedOut -> {
            onSignOut()
        }

        is ProfileState.Success -> {
            val user = (state as ProfileState.Success).profile
            val blogs = (state as ProfileState.Success).blogs
            val isMe = user.id == SupabaseClient.client.auth.currentUserOrNull()?.id

            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (isMe)
                                "My Profile" else "@${user.displayName}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    actions = {
                        if (isMe) {
                            IconButton(onClick = {
                                showDialog = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logout_box_r_line),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (!isMe) {
                            IconButton(onClick = {
                                onNavigateBack()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_left_line),
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }, content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        // Avatar & basic info
                        AvatarImage(
                            randomSeed = user.avatarId,
                        )

                        Text(
                            text = "@${user.displayName}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = "Joined ${convertTimestampToHumanReadableFormat(user.createdAt)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = if (user.bio.isNullOrBlank()) "No Bio Yet" else user.bio,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                        )
                        if (isMe) {
                            Button(
                                onClick = {
                                    onClickEditProfile()
                                },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .border(
                                        width = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .height(35.dp)
                            ) {
                                Text(
                                    text = "Edit Profile",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Text(
                            text = if (isMe) "My Blogs" else "Blogs",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    top = if (user.bio.isNullOrBlank()) 0.dp else 24.dp
                                ),
                            textAlign = TextAlign.Start
                        )
                    }

                    if (blogs.isNullOrEmpty()) {
                        if (isMe) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 17.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.undraw_blog_post_f68f),
                                        contentDescription = "No blogs illustration",
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .aspectRatio(1.8f)
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Text(
                                        text = "No Blogs Yet",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "Start sharing your thoughts and ideas by creating your first blog post.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Button(
                                        onClick = {
                                            onCreateBlog()
                                        },
                                        shape = RoundedCornerShape(4.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        contentPadding = PaddingValues(horizontal = 4.dp),
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .border(
                                                width = 0.5.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .height(35.dp)
                                    ) {
                                        Text(
                                            text = "Create Blog",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }

                        } else {
                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No Blogs Yet",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(blogs.size) { index ->
                            val blog = blogs[index]

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                                    .border(
                                        0.1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        onClickUserBlog(blog.id)
                                    }) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = blog.title,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = blog.description,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    Text(
                                        text = formatCustomDateTime(blog.publishDate),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }
                        }

                    }

                }
                if (showDialog) {
                    BasicAlertDialog(
                        onDismissRequest = { showDialog = false },
                        content = {
                            Surface(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight(),
                                shape = MaterialTheme.shapes.medium,
                                tonalElevation = AlertDialogDefaults.TonalElevation
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Sign Out",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Are you sure you want to sign out?",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { showDialog = false }) {
                                            Text("Cancel")
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        TextButton(
                                            onClick = {
                                                showDialog = false
                                                onConfirmSignOut()
                                            },
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.error
                                            )
                                        ) {
                                            Text("Sign Out")
                                        }
                                    }
                                }
                            }
                        }
                    )
                }

            })
        }

        is ProfileState.Error -> {
            val error = (state as ProfileState.Error).message
            println(error)
        }

        else -> {}
    }
}
