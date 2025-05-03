package com.mindpalace.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockComponent(
    richTextState: RichTextState,
    onNormalInsert: () -> Unit,
    onDeleteBlock: () -> Unit,
    focusRequester: FocusRequester,
    onFocused: () -> Unit,
) {
    var lastText by remember { mutableStateOf("") }

    LaunchedEffect(richTextState.annotatedString) {
        val currentText = richTextState.annotatedString.toString()
        if (lastText.isNotEmpty() && currentText.isEmpty()) {
            onDeleteBlock()
        }
        lastText = currentText
    }

    Box {
        RichTextEditor(
            state = richTextState,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .border(1.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onFocused()
                    }
                }
                .focusRequester(focusRequester),
            keyboardActions = KeyboardActions(
                onNext = {
                    if (!richTextState.isOrderedList) {
                        // Handle the normal text insert here
                        onNormalInsert()
                    } else {
                        val currentText = richTextState.annotatedString.toString()
                        val newText = "$currentText\n"
                        richTextState.setText(newText)
                    }
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            placeholder = {
                Text(
                    text = "Start writing…",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            },
            textStyle = MaterialTheme.typography.titleSmall,
            colors = RichTextEditorDefaults.richTextEditorColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
            ),
        )
    }
}

