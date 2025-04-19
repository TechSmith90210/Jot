package com.mindpalace.app.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.mindpalace.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (navController: NavController) {
    LaunchedEffect(true) {
        delay(3500) // Splash screen delay (2 seconds)
        navController.navigate("welcomeScreen") {
            popUpTo("splashScreen") { inclusive = true } // remove splash from backstack
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.brain_gif)
                .decoderFactory(factory = GifDecoder.Factory())
                .build(),
            contentDescription = "Splash Logo",
            modifier = Modifier.size(100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview () {
    SplashScreen(navController = rememberNavController())
}