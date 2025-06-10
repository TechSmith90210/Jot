package com.mindpalace.app.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mindpalace.app.R
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun BlockToolbar(
    richTextState: RichTextState, onBlockEditorClose: () -> Unit,
    onDeleteBlock: () -> Unit, onMoveUp: () -> Unit, onMoveDown: () -> Unit
) {

    val textLength = richTextState.annotatedString.text.length

    val currentSpanStyle = richTextState.currentSpanStyle

    val isHeading = currentSpanStyle.fontWeight == FontWeight.Bold &&
            currentSpanStyle.fontSize == 26.sp

    val isOrderedList = richTextState.isOrderedList
    val isUnorderedList = richTextState.isUnorderedList


    val quoteStyle = SpanStyle(
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )
    val isQuote = richTextState.currentSpanStyle.fontStyle == quoteStyle.fontStyle &&
            richTextState.currentSpanStyle.color == quoteStyle.color

    val codeBlockStyle = SpanStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        background = Color.DarkGray,
        textDecoration = TextDecoration.None,
    )
    val isCodeBlock = richTextState.currentSpanStyle == codeBlockStyle

    // Heading Button
    IconButton(
        onClick = {
            if (isHeading
            ) {
                richTextState.clearSpanStyles(
                    textRange = TextRange(
                        0, textLength
                    )
                )
            } else {
                richTextState.addSpanStyle(
                    spanStyle = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                    textRange = TextRange(
                        0, textLength
                    )
                )
            }

        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isHeading) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
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
            if (isOrderedList) {
                richTextState.removeOrderedList()
            } else {
                // Ensure unordered list is not active
                if (isUnorderedList) {
                    richTextState.removeUnorderedList()
                }
                richTextState.addOrderedList()
            }
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isOrderedList) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.list_ordered_2),
            contentDescription = "Numbered List",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    // Unordered List Button
    IconButton(
        onClick = {
            if (isUnorderedList) {
                richTextState.removeUnorderedList()
            } else {
                if (isOrderedList) {
                    richTextState.removeOrderedList()
                }
                richTextState.addUnorderedList()
            }
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isUnorderedList)
                MaterialTheme.colorScheme.outline
            else
                MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.list_unordered),
            contentDescription = "Unordered List",
            tint = MaterialTheme.colorScheme.secondary
        )
    }


    // Quote Button
    IconButton(
        onClick = {
            if (isQuote
            ) {
                richTextState.clearSpanStyles(
                    textRange = TextRange(
                        0, textLength
                    )
                )
            } else {
                richTextState.addSpanStyle(
                    spanStyle = quoteStyle,
                    textRange = TextRange(
                        0, textLength
                    )
                )
            }
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isQuote) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
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
            if (isCodeBlock) {
                richTextState.clearSpanStyles(
                    textRange = TextRange(
                        0, textLength
                    )
                )
            } else {
                richTextState.addSpanStyle(
                    codeBlockStyle,
                    textRange = TextRange(0, textLength)
                )
            }
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isCodeBlock) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.background,
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
            painter = painterResource(id = R.drawable.arrow_up_line),
            contentDescription = "Move Block Up",
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
            painter = painterResource(id = R.drawable.arrow_down_line),
            contentDescription = "Move Block Down",
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