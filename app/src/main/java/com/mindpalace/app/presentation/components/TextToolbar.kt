package com.mindpalace.app.presentation.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.mindpalace.app.R
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun TextToolbar(
    richTextState: RichTextState, keyboardController: SoftwareKeyboardController?,
    onBlockEditingSectionOpen: () -> Unit, onColorPickerBarOpen: () -> Unit
) {
    val currentColor : Color = richTextState.currentSpanStyle.color
    val isBold by remember {
        derivedStateOf {
            richTextState.currentSpanStyle.fontWeight == FontWeight.Bold
        }
    }

    val isItalic by remember {
        derivedStateOf {
            richTextState.currentSpanStyle.fontStyle == FontStyle.Italic
        }
    }

    val isUnderline by remember {
        derivedStateOf {
            richTextState.currentSpanStyle.textDecoration == TextDecoration.Underline
        }
    }

    // Bold Button
    IconButton(
        onClick = {
            richTextState.toggleSpanStyle(
                SpanStyle(fontWeight = FontWeight.Bold)
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isBold) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.bold),
            contentDescription = "Bold",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Italic Button
    IconButton(
        onClick = {
            richTextState.toggleSpanStyle(
                SpanStyle(fontStyle = FontStyle.Italic)
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isItalic) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.italic),
            contentDescription = "Italics",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Underline Button
    IconButton(
        onClick = {
            richTextState.toggleSpanStyle(
                SpanStyle(textDecoration = TextDecoration.Underline)
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isUnderline) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.underline),
            contentDescription = "Underline",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    IconButton(
        onClick = {},
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isUnderline) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Box(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Normal tap action
                        richTextState.toggleSpanStyle(SpanStyle(color = currentColor))
                    },
                    onLongPress = {
                        // Long press action
                        onColorPickerBarOpen()
                    }
                )
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.font_color),
                contentDescription = "Text Color",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }



    //StrikeThrough
    IconButton(
        onClick = {
            richTextState.toggleSpanStyle(
                SpanStyle(
                    textDecoration = TextDecoration.LineThrough
                )
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isUnderline) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.strikethrough),
            contentDescription = "StrikeThrough",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Open Text Editing Section Button
    IconButton(
        onClick = {
            onBlockEditingSectionOpen()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add_fill),
            contentDescription = "Text Formatting Options",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Hide Keyboard Button
    IconButton(
        onClick = {
            keyboardController?.hide()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.keyboard_line),
            contentDescription = "Hide Keyboard",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

}