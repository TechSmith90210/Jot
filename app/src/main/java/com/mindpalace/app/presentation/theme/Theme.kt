package com.mindpalace.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your custom color palette
private val LightColors = lightColorScheme(
    primary = Color(0xFF2C2C2C),       // Primary - Dark Gray
    secondary = Color(0xFF655560),     // Secondary - Muted Purple
    background = Color(0xFFFCF7FF),    // Background - Soft White
    surface = Color(0xFFF5F5F5),       // Surface - Very Light Gray
    onPrimary = Color.White,           // Text on primary
    onSecondary = Color.White,
    onBackground = Color(0xFF2C2C2C),  // Foreground - Dark Gray
    onSurface = Color(0xFF2C2C2C),
)

@Composable
fun MindPalaceTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = LightColors // Force light theme for now, or add a darkColorScheme if needed

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
