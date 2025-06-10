package com.mindpalace.app.presentation.screens.onboarding.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.mindpalace.app.R

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    LaunchedEffect(isUserLoggedIn) {
        when (isUserLoggedIn) {
            true -> onNavigateToHome()
            false -> onNavigateToWelcome()
            null -> {
                // Optionally, add a delay or a loading screen if session is still loading
                // You can show a loading indicator here instead of doing nothing
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // If the session state is null, you can show a loading indicator
        if (isUserLoggedIn == null) {
            CircularProgressIndicator() // Show a loading indicator while checking session
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.brain_gif)
                    .decoderFactory(factory = GifDecoder.Factory())
                    .crossfade(true) // Adds crossfade animation while loading
                    .build(),
                contentDescription = "Splash Logo",
                modifier = Modifier.size(90.dp)
            )
        }
    }
}
