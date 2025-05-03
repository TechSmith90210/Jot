package com.mindpalace.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun ColorPickerBar(
    richTextState: RichTextState,
    onColorBarClose: () -> Unit
) {
    val colors = listOf(
        Color(0xFFB71C1C), // Dark Red
        Color(0xFF0D47A1), // Dark Blue
        Color(0xFF1B5E20), // Dark Green
        Color(0xFF880E4F), // Dark Magenta
        Color(0xFFFF8C00), // Dark Orange (darker orange)
        Color(0xFF4B0082)  // Dark Slate Blue
    )

    // Color Circles
    colors.forEach { color ->
        ColorCircle(color = color) {
            richTextState.toggleSpanStyle(
                SpanStyle(color = color)
            )
        }
    }
    // Close Button
    IconButton(
        onClick = onColorBarClose,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_left_line),
            contentDescription = "Close Editing Section",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun ColorCircle(color: Color, onClick: () -> Unit) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(15.dp)
            .background(color, shape = CircleShape)
            .clickable(onClick = onClick)
    )
}
