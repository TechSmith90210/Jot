package com.mindpalace.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF2C2C2C),        // Primary - Dark Gray
    secondary = Color(0xFF655560),      // Secondary - Muted Purple
    background = Color(0xFFFCF7FF),     // Background - Soft White
    surface = Color(0xFFF5F5F5),        // Surface - Very Light Gray
    onPrimary = Color(0xFF2C2C2C),      // TextPrimary - Dark Gray
    onSecondary = Color(0xFF878C8F),    // TextSecondary - Muted Gray
    tertiary = Color(0xFFA4969B),       // Accent - Soft Purple
    outline = Color(0xFFD3D3D3),        // borders - Muted Gray
    outlineVariant = Color(0xFFC4CAD0), // disabled - Faded Gray
)

// Dark theme colors
private val DarkColors = darkColorScheme(
    primary = Color(0xFFFCF7FF),        // Light Text for buttons (inverse of Light Primary)
    secondary = Color(0xFFD1C6CE),      // Muted Lavender-ish for secondary actions
    background = Color(0xFF1A1A1A),     // Very dark background for app
    surface = Color(0xFF2C2C2C),        // Cards & panels (Dark Gray)
    onPrimary = Color(0xFF2C2C2C),      // Dark text on light-colored buttons
    onSecondary = Color(0xFF2C2C2C),    // Dark text on lighter secondary buttons
    tertiary = Color(0xFFB9A9B0),       // Softer version of accent for dark mode
    outline = Color(0xFF4D4D4D),        // Dark muted gray for borders
    outlineVariant = Color(0xFF5E5E5E), // Slightly brighter for disabled state
)

@Composable
fun MindPalaceTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
