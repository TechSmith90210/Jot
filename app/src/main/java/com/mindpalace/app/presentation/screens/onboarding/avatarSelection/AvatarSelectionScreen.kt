package com.mindpalace.app.presentation.screens.onboarding.avatarSelection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AvatarImage
import com.mindpalace.app.presentation.components.LoadingScreen
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarSelectorScreen(
    modifier: Modifier, viewModel: AvatarSelectionViewModel = hiltViewModel(),
    navController: NavController
) {
    var randomSeed by remember { mutableStateOf(UUID.randomUUID().toString()) }
    val avatarSelectionState by viewModel.avatarSelectionState.collectAsState()

    fun onContinueClick(avatarId: String) {
        viewModel.updateAvatar(avatarId)
    }

    when (avatarSelectionState) {
        is AvatarSelectionState.Success -> {
            navController.navigate("homeScreen")
        }

        is AvatarSelectionState.Loading -> {
            LoadingScreen()
        }

        is AvatarSelectionState.Error -> {
            val errorMessage = (avatarSelectionState as AvatarSelectionState.Error).message
            Text(text = errorMessage)
        }

        is AvatarSelectionState.Idle -> {
            Scaffold(topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ), title = {
                        Text(
                            text = "Choose Your Avatar",
                            style = MaterialTheme.typography.titleSmall
                        )
                    })
            }, content = { padding ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    AvatarImage(randomSeed = randomSeed)

                    Spacer(Modifier.height(40.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround, content = {
                            Button(
                                onClick = {
                                    randomSeed = UUID.randomUUID().toString()
                                    print(randomSeed)

                                },
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary,
                                    disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.refresh_line), // official "G" icon
                                    contentDescription = "Refresh Avatar",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Regenerate Avatar",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = { onContinueClick(randomSeed) },
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                            ) {
                                Text("Continue", style = MaterialTheme.typography.labelLarge)
                            }
                        })

                    Spacer(Modifier.height(15.dp))

                    Text(
                        text = "Change your avatar anytime in profile settings!",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            })
        }
    }
}