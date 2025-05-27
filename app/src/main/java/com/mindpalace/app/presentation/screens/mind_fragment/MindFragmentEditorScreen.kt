package com.mindpalace.app.presentation.screens.mind_fragment

import MindFragmentJson
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.core.formatCustomDateTime
import com.mindpalace.app.presentation.components.BlockComponent
import com.mindpalace.app.presentation.components.EditingToolbar
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import java.util.UUID

data class RichTextBlock(
    val id: String = UUID.randomUUID().toString(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindFragmentEditorScreen(
    id: String,
    onNavigateBack: () -> Unit,
    mindFragmentViewModel: MindFragmentViewModel = hiltViewModel()
) {
    // List of blocks
    val blocks = remember { mutableStateListOf<RichTextBlock>().apply { add(RichTextBlock()) } }

    // Map to store state for each block
    val blockStates = remember { mutableStateMapOf<String, RichTextState>() }

    // Focus management
    val focusRequesters = remember { mutableStateMapOf<String, FocusRequester>() }
    var focusBlockId by remember { mutableStateOf<String?>(null) }

    // Title state
    var title by remember { mutableStateOf("") }
    var lastSavedTitle by remember { mutableStateOf("") }

    val fragment by mindFragmentViewModel.fragment.collectAsState()

    var saveJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun onTitleChanged(newTitle: String) {
        title = newTitle
        saveJob?.cancel()
        saveJob = coroutineScope.launch {
            delay(2000L)

            if (newTitle != lastSavedTitle) {
                lastSavedTitle = newTitle
                mindFragmentViewModel.updateFragment(
                    fragment?.copy(title = newTitle) ?: return@launch
                )
            }
        }
    }

    LaunchedEffect(id) {
        mindFragmentViewModel.getFragmentById(id)
    }

    LaunchedEffect(fragment) {
        fragment?.let { frag ->
            title = frag.title

            val parsed = Json.decodeFromString<MindFragmentJson>(frag.content)

            blocks.clear()
            blocks.addAll(parsed.blocks.map { RichTextBlock(it.id) })

            parsed.blocks.forEachIndexed { index, blockJson ->
                // Assuming blockStates is a MutableMap<String, RichTextState> or similar
                blockStates[blockJson.id] = RichTextState().apply {
                    setHtml(blockJson.text)
                }
            }

            // Don't clear blockStates or focusRequesters here; they'll be created in the lazy list
        }
    }

    LaunchedEffect(Unit) {
        var lastSavedContent: String? = null

        while (true) {
            val blockArray = buildJsonArray {
                for (block in blocks) {
                    val state = blockStates[block.id]
                    add(
                        buildJsonObject {
                            put("id", JsonPrimitive(block.id))
                            put("text", JsonPrimitive(state?.toHtml() ?: ""))
                        })
                }
            }

            val currentJson = buildJsonObject {
                put("title", JsonPrimitive(title))
                put("blocks", blockArray)
            }.toString()

            if (currentJson != lastSavedContent && fragment != null) {
                lastSavedContent = currentJson

                println("Saving updated note JSON: $currentJson")

                val updatedFragment = fragment!!.copy(
                    title = title, content = currentJson.toString()
                )

                mindFragmentViewModel.updateFragment(updatedFragment)
            }

            delay(4000)
        }
    }

    var sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    fun onConfirmDelete() {
        mindFragmentViewModel.deleteFragment(
            fragment?.userId ?: "", fragment?.id ?: ""
        )
        onNavigateBack()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    if (title.isEmpty()) "New Fragment" else title,
                    style = MaterialTheme.typography.labelLarge,
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            ), navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_line),
                        contentDescription = "Back"
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    showBottomSheet = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_line),
                        contentDescription = "more",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            })
    }, content = { padding ->
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            item {
                BasicTextField(
                    value = title,
                    onValueChange = { newTitle ->
                        onTitleChanged(newTitle)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground, letterSpacing = 1.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "Title...",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    })
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Loop through each block and display it
            itemsIndexed(
                items = blocks, key = { _, block -> block.id }) { index, block ->
                val blockState = blockStates.getOrPut(block.id) { rememberRichTextState() }
                val focusRequester = focusRequesters.getOrPut(block.id) { FocusRequester() }

                // Focus management: set focus on the current block when necessary
                LaunchedEffect(focusBlockId) {
                    if (focusBlockId == block.id) {
                        focusRequester.requestFocus()
                    }
                }

                BlockComponent(richTextState = blockState, onNormalInsert = {
                    val newBlock = RichTextBlock()
                    blocks.add(index + 1, newBlock)
                    focusBlockId = newBlock.id
                }, onDeleteBlock = {
                    if (blocks.size > 1) {
                        blocks.removeAt(index)
                        focusBlockId = when {
                            index > 0 -> blocks[index - 1].id
                            blocks.isNotEmpty() -> blocks[0].id
                            else -> null
                        }
                    }
                }, focusRequester = focusRequester, onFocused = {
                    focusBlockId = block.id
                })
            }

            item {
                TextButton(
                    onClick = {
                        val newBlock = RichTextBlock()
                        blocks.add(newBlock)
                        focusBlockId = newBlock.id
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_fill),
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Block",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .systemBarsPadding(),
                containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(9.dp),
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        ListItem(
                            modifier = Modifier.clickable {
                                showDialog = true

                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ), headlineContent = {
                                Text(
                                    "Delete Fragment",
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }, leadingContent = {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete_bin_6_line),
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            })
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Fragment Info",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Created:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = formatCustomDateTime(fragment?.createdAt ?: "Unknown"),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

        }
        if (showDialog) {
            BasicAlertDialog(
                onDismissRequest = { showDialog = false },
                content = {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Delete Fragment?",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Are you sure you want to delete this fragment? This action cannot be undone.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("Cancel")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                        onConfirmDelete()
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            )
        }

    }, bottomBar = {
        val focusedState = focusBlockId?.let { blockStates[it] }
        EditingToolbar(focusedState ?: rememberRichTextState(), onDeleteBlock = {
            val index = blocks.indexOfFirst { it.id == focusBlockId }
            if (index >= 0 && blocks.size > 1) {
                blocks.removeAt(index)
                focusBlockId = when {
                    index > 0 -> blocks[index - 1].id
                    blocks.isNotEmpty() -> blocks[0].id
                    else -> null
                }
            }
        }, onMoveUp = {
            val index = blocks.indexOfFirst { it.id == focusBlockId }
            if (index > 0) {
                val block = blocks.removeAt(index)
                blocks.add(index - 1, block)
                focusBlockId = block.id
            }
        }, onMoveDown = {
            val index = blocks.indexOfFirst { it.id == focusBlockId }
            if (index >= 0 && index < blocks.lastIndex) {
                val block = blocks.removeAt(index)
                blocks.add(index + 1, block)
                focusBlockId = block.id
            }
        })
    })
}
