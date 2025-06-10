@file:OptIn(ExperimentalMaterial3Api::class)

package com.mindpalace.app.presentation.screens.profile.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AppTextField
import com.mindpalace.app.presentation.components.AvatarImage
import com.mindpalace.app.presentation.components.LoadingScreen
import com.mindpalace.app.presentation.screens.profile.ProfileState
import com.mindpalace.app.presentation.screens.profile.ProfileViewModel
import java.util.UUID

@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()
    var isUpdating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { profileViewModel.fetchCurrentUser() }

    when (state) {
        is ProfileState.Loading -> LoadingScreen()
        is ProfileState.Error -> {
            val error = (state as ProfileState.Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error)
            }
        }

        is ProfileState.ProfileUpdated -> {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                profileViewModel.fetchCurrentUser() // Refresh to return to Success state
            }

            // Optional: Show a loading indicator or just wait for fetch
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ProfileState.Success -> {
            val user = (state as ProfileState.Success).profile

            var randomSeed by remember { mutableStateOf(user.avatarId) }
            val displayName = remember { mutableStateOf(user.displayName) }
            val bio = remember { mutableStateOf(user.bio ?: "") }
            val email = remember { mutableStateOf(user.email) }

            val isChanged = remember(randomSeed, displayName.value, bio.value) {
                randomSeed != user.avatarId ||
                        displayName.value != user.displayName ||
                        bio.value != (user.bio ?: "")
            }
            var displayNameError by remember { mutableStateOf<String?>(null) }

            fun onUpdateProfile() {
                if (displayName.value.trim().length < 8) {
                    displayNameError = "Display name must be at least 8 characters"
                    return
                }

                displayNameError = null
                isUpdating = true
                profileViewModel.updateProfile(
                    avatarId = randomSeed,
                    displayName = displayName.value.trim(),
                    bio = bio.value.trim()
                )
                isUpdating = false
            }


            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Edit Profile",
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    painter = painterResource(R.drawable.arrow_left_line),
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                content = { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = padding.calculateTopPadding())
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Box(modifier = Modifier.size(100.dp)) {
                                AvatarImage(randomSeed = randomSeed)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { randomSeed = UUID.randomUUID().toString() },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.refresh_line),
                                    contentDescription = "Refresh Avatar",
                                    modifier = Modifier.size(15.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Regenerate Avatar",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            AppTextField(
                                value = email.value,
                                onValueChange = { email.value = it },
                                label = "Email",
                                isDisabled = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Column {
                                AppTextField(
                                    value = displayName.value,
                                    onValueChange = {
                                        displayName.value = it
                                        if (it.length >= 8) displayNameError = null
                                    },
                                    label = "Display Name",
                                    modifier = Modifier.fillMaxWidth()
                                )
                                if (displayNameError != null) {
                                    Text(
                                        text = displayNameError!!,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            AppTextField(
                                value = bio.value,
                                onValueChange = { bio.value = it },
                                label = "Bio",
                                modifier = Modifier.fillMaxWidth(),
                                isMultiline = true
                            )
                        }
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (!isUpdating) onUpdateProfile() },
                            enabled = isChanged && !isUpdating,
                            modifier = Modifier.fillMaxWidth(0.7f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.12f
                                ),
                                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.38f
                                )
                            )
                        ) {
                            Text("Update Profile", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            )
        }

        else -> Unit
    }
}
