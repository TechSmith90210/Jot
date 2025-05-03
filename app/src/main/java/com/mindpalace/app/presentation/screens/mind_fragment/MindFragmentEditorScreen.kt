package com.mindpalace.app.presentation.screens.mind_fragment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.BlockComponent
import com.mindpalace.app.presentation.components.EditingToolbar
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import java.util.UUID

data class RichTextBlock(
    val id: String = UUID.randomUUID().toString(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MindFragmentEditorScreen(id: String, onNavigateBack: () -> Unit) {

    val blocks = remember { mutableStateListOf<RichTextBlock>().apply { add(RichTextBlock()) } }
    val blockStates = remember { mutableStateMapOf<String, RichTextState>() }
    val focusRequesters = remember { mutableStateMapOf<String, FocusRequester>() }
    var focusBlockId by remember { mutableStateOf<String?>(null) }
    var title by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (title.isEmpty()) "New Fragment" else title,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Title Field
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 1.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "Title...",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    state = rememberLazyListState()
                ) {
                    itemsIndexed(
                        items = blocks,
                        key = { _, block -> block.id }
                    ) { index, block ->
                        val blockState = blockStates.getOrPut(block.id) { rememberRichTextState() }
                        val focusRequester = focusRequesters.getOrPut(block.id) { FocusRequester() }

                        LaunchedEffect(focusBlockId) {
                            if (focusBlockId == block.id) {
                                focusRequester.requestFocus()
                            }
                        }

                        BlockComponent(
                            richTextState = blockState,
                            onNormalInsert = {
                                val newBlock = RichTextBlock()
                                blocks.add(index + 1, newBlock)
                                focusBlockId = newBlock.id
                            },
                            onDeleteBlock = {
                                if (blocks.size > 1) {
                                    blocks.removeAt(index)
                                    focusBlockId = when {
                                        index > 0 -> blocks[index - 1].id
                                        blocks.isNotEmpty() -> blocks[0].id
                                        else -> null
                                    }
                                }
                            },
                            focusRequester = focusRequester,
                            onFocused = {
                                focusBlockId = block.id
                            },

                            )
                    }
                }

//                // Button to add a new block at the end
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp)
//                        .clickable(
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                        ) {
//                            val newBlock = RichTextBlock()
//                            blocks.add(newBlock)
//                            focusBlockId = newBlock.id
//                        }
//                ) {
//                    Text("Add New Block", style = MaterialTheme.typography.bodyMedium, color  = MaterialTheme.colorScheme.secondary)
//                }
            }
        },
        bottomBar = {
            val focusedState = focusBlockId?.let { blockStates[it] }
            EditingToolbar(
                focusedState ?: rememberRichTextState(),

                onDeleteBlock = {
                    val index = blocks.indexOfFirst { it.id == focusBlockId }
                    if (blocks.size > 1) {
                        blocks.removeAt(blocks.indexOfFirst { it.id == focusBlockId })
                    }
                    focusBlockId = when {
                        index > 0 -> blocks[index - 1].id
                        blocks.isNotEmpty() -> blocks[0].id
                        else -> null
                    }

                },
                onMoveUp = {
                    val index = blocks.indexOfFirst { it.id == focusBlockId }
                    if (index > 0) {
                        val block = blocks.removeAt(index)
                        blocks.add(index - 1, block)
                        focusBlockId = block.id
                    }
                },
                onMoveDown = {
                    val index = blocks.indexOfFirst { it.id == focusBlockId }
                    if (index >= 0 && index < blocks.lastIndex) {
                        val block = blocks.removeAt(index)
                        blocks.add(index + 1, block)
                        focusBlockId = block.id
                    }
                }
            )

        })
}