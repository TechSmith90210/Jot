package com.mindpalace.app.presentation.screens.mind_fragment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindFragmentEditorScreen(id: String, onNavigateBack: () -> Unit) {
    var title by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val richTextState = rememberRichTextState()

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(id, style = MaterialTheme.typography.labelSmall) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left_line),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.more_line),
                            contentDescription = "more",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxSize()
            ) {
                // Title Field
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp,
                        ),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 1.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "Enter a title...",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                )

                // Rich Text Editor
                RichTextEditor(
                    state = richTextState,
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 8.dp),
                    placeholder = {
                        Text(
                            text = "Start writing your thoughts...",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    },
                    contentPadding = PaddingValues(top = 8.dp),
                    textStyle = MaterialTheme.typography.displaySmall.copy(
                        lineHeight = MaterialTheme.typography.displaySmall.lineHeight * 2
                    ),
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                    ),
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = imeVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .imePadding()
                        .navigationBarsPadding(),
                    content = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        )
                        {
                            // Heading Button
                            IconButton(
                                onClick = {
                                    richTextState.toggleSpanStyle(
                                        SpanStyle(fontWeight = FontWeight.Bold)
                                    )
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (isBold) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.heading),
                                    contentDescription = "Heading",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Bold Button
                            IconButton(
                                onClick = {
                                    richTextState.toggleSpanStyle(
                                        SpanStyle(fontWeight = FontWeight.Bold)
                                    )
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (isBold) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
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
                                    containerColor = if (isItalic) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
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
                                    containerColor = if (isUnderline) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.underline),
                                    contentDescription = "Underline",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Numbered List Button
                            IconButton(
                                onClick = {
                                    richTextState.toggleSpanStyle(
                                        SpanStyle(textDecoration = TextDecoration.Underline)
                                    )
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (isUnderline) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.list_ordered),
                                    contentDescription = "Numbered List",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Text Color Button
                            IconButton(
                                onClick = {
                                    richTextState.toggleSpanStyle(
                                        SpanStyle(textDecoration = TextDecoration.Underline)
                                    )
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (isUnderline) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.font_color),
                                    contentDescription = "Quote Text",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Quote Button
                            IconButton(
                                onClick = {
                                    richTextState.toggleSpanStyle(
                                        SpanStyle(textDecoration = TextDecoration.Underline)
                                    )
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (isUnderline) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
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
                                    richTextState.toggleSpanStyle(
                                        SpanStyle(fontWeight = FontWeight.Bold)
                                    )
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (isBold) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.code_s_slash_line),
                                    contentDescription = "Code Block",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Delete Button (placeholder action)
                            IconButton(
                                onClick = {},
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete_bin_6_line),
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Keyboard down
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
                                    contentDescription = "Keyboard hide",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                    }
                )
            }
        }
    )
}
