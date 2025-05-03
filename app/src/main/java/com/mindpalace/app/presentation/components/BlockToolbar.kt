package com.mindpalace.app.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mindpalace.app.R
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun BlockToolbar(richTextState: RichTextState, onBlockEditorClose: () -> Unit,
                 onDeleteBlock: () -> Unit, onMoveUp : ()-> Unit, onMoveDown : ()-> Unit) {

    // Heading Button
    IconButton(
        onClick = {
            richTextState.toggleSpanStyle(
                spanStyle = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp),
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.heading),
            contentDescription = "Heading",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Numbered List Button
    IconButton(
        onClick = {
            richTextState.clear()
            richTextState.toggleOrderedList(
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.list_ordered),
            contentDescription = "Numbered List",
            tint = MaterialTheme.colorScheme.secondary
        )
    }


    // Quote Button
    IconButton(
        onClick = {},
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.quote_text),
            contentDescription = "Quote Text",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Code Block Button
    IconButton(
        onClick = {
            richTextState.addSpanStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    background = Color.DarkGray,
                    textDecoration = TextDecoration.None,
                )
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.code_s_slash_line),
            contentDescription = "Code Block",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    IconButton(
        onClick = {
            onMoveUp()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.skip_up_line),
            contentDescription = "Code Block",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    IconButton(
        onClick = {
            onMoveDown()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.skip_down_line),
            contentDescription = "Code Block",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    IconButton(
        onClick = {
            onDeleteBlock()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.delete_bin_6_line),
            contentDescription = "Delete Block",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Close Button (close editing section)
    IconButton(
        onClick = {
            onBlockEditorClose()
        },
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