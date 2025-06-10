package com.mindpalace.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun EditingToolbar(richTextState: RichTextState, onDeleteBlock: () -> Unit, onMoveUp : ()-> Unit,
                   onMoveDown : ()-> Unit
                   ) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    var isBlockEditingSectionOpen by remember { mutableStateOf(false) }

    val onBlockEditorClose: () -> Unit = {
        isBlockEditingSectionOpen = false
    }

    val onBlockEditingSectionOpen: () -> Unit = {
        isBlockEditingSectionOpen = true
    }

    var isColorPickerBarOpen by remember { mutableStateOf(false) }

    val onColorBarClose: () -> Unit = {
        isColorPickerBarOpen = false
    }

    val onColorPickerBarOpen: () -> Unit = {
        isColorPickerBarOpen = true
    }



    AnimatedVisibility(
        visible = imeVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .imePadding()
                .height(45.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = Color.Gray.copy(0.2f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isBlockEditingSectionOpen) {
                        BlockToolbar(
                            richTextState = richTextState,
                            onBlockEditorClose = onBlockEditorClose,
                            onDeleteBlock = onDeleteBlock,
                            onMoveUp = onMoveUp,
                            onMoveDown = onMoveDown
                        )
                    } else if (isColorPickerBarOpen) {
                        ColorPickerBar(
                            richTextState = richTextState,
                            onColorBarClose = onColorBarClose,
                        )
                    } else {
                        // Show basic options like Bold, Italic, etc.
                        TextToolbar(
                            richTextState,
                            keyboardController,
                            onBlockEditingSectionOpen,
                            onColorPickerBarOpen,
                        )
                    }
                }
            }
        )
    }
}

