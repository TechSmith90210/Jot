package com.mindpalace.app.presentation.screens.blog

import BlockJson
import MindFragmentJson
import androidx.compose.foundation.border
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
import androidx.compose.material3.MediumTopAppBar
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.core.formatCustomDateTime
import com.mindpalace.app.presentation.components.BlockComponent
import com.mindpalace.app.presentation.components.EditingToolbar
import com.mindpalace.app.presentation.components.RichTextBlock
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindBlogEditorScreen(
    blogId: String,
    onNavigateBack: () -> Unit,
    viewModel: BlogViewModel = hiltViewModel(),
) {
    val blog by viewModel.blog.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // UI state
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val blocks = remember { mutableStateListOf(RichTextBlock()) }
    val blockStates = remember { mutableStateMapOf<String, RichTextState>() }
    val focusRequesters = remember { mutableStateMapOf<String, FocusRequester>() }
    var focusBlockId by remember { mutableStateOf<String?>(null) }

    var saveJob by remember { mutableStateOf<Job?>(null) }
    var lastSavedState by remember { mutableStateOf("") }

    val auth: Auth = SupabaseClient.client.auth
    val currentUserId =
        auth.currentUserOrNull()?.id ?: return
    val isEditor = blog?.authorId == currentUserId


    var sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    fun onConfirmDelete() {
        viewModel.deleteBlog(
            blogId
        )
        onNavigateBack()
    }

    LaunchedEffect(blockStates, title, description) {
        while (true) {
            delay(2000L)

            // Construct new content
            val currentState = Json.encodeToString(
                MindFragmentJson(
                    title = title,
                    blocks = blocks.map {
                        BlockJson(it.id, blockStates[it.id]?.toHtml() ?: "")
                    }
                )
            )

            // Only save if something changed
            if (currentState != lastSavedState) {
                lastSavedState = currentState
                blog?.let {
                    val updated =
                        it.copy(title = title, description = description, content = currentState)
                    viewModel.updateBlog(updated)
                }
            }
        }
    }
    fun scheduleAutoSave() {
        saveJob?.cancel()
        saveJob = coroutineScope.launch {
            blog?.let {
                val content = Json.encodeToString(
                    MindFragmentJson(
                        // You can rename this to `MindBlogJson` if needed
                        blocks = blocks.map {
                            BlockJson(it.id, blockStates[it.id]?.toHtml() ?: "")
                        },
                        title = title,
                    )
                )
                val updated = it.copy(title = title, description = description, content = content)
                viewModel.updateBlog(updated)
            }
        }
    }

    LaunchedEffect(blogId) {
        viewModel.getBlogById(blogId)
    }

    LaunchedEffect(blog) {
        blog?.let {
            title = it.title
            description = it.description
            blocks.clear()
            val parsed = Json.decodeFromString<MindFragmentJson>(it.content)
            blocks.addAll(parsed.blocks.map { RichTextBlock(it.id) })
            parsed.blocks.forEach {
                blockStates[it.id] = RichTextState().apply { setHtml(it.text) }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            MediumTopAppBar(
                modifier = Modifier.border(
                    width = 0.1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                ),
                title = {
                    BasicTextField(
                        readOnly = !isEditor,
                        value = description,
                        onValueChange = {
                            description = it
                            scheduleAutoSave()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            if (description.isEmpty()) {
                                Text(
                                    "Add a short description...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        })
                }, actions = {
                    IconButton(
                        onClick = {
                            showBottomSheet = true
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.more_line), contentDescription = ""
                        )
                    }
                }, navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left_line),
                            contentDescription = "Back"
                        )
                    }
                }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                scrollBehavior = scrollBehavior
            )
        }, content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(top=padding.calculateTopPadding())
                    .fillMaxSize()
            ) {
                item {
                    BasicTextField(
                        readOnly = !isEditor,
                        value = title,
                        onValueChange = {
                            title = it
                            scheduleAutoSave()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 14.dp),
                        textStyle = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text(
                                    "Blog Title...",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        })
                }

                itemsIndexed(blocks, key = { _, block -> block.id }) { index, block ->
                    val state = blockStates.getOrPut(block.id) { rememberRichTextState() }
                    val focusRequester = focusRequesters.getOrPut(block.id) { FocusRequester() }

                    LaunchedEffect(focusBlockId) {
                        if (focusBlockId == block.id) focusRequester.requestFocus()
                    }

                    BlockComponent(
                        isEditor = isEditor,
                        richTextState = state, onNormalInsert = {
                            val newBlock = RichTextBlock()
                            blocks.add(index + 1, newBlock)
                            focusBlockId = newBlock.id
                        }, onDeleteBlock = {
                            if (blocks.size > 1) {
                                blocks.removeAt(index)
                                focusBlockId = blocks.getOrNull(index - 1)?.id
                            }
                        }, focusRequester = focusRequester, onFocused = { focusBlockId = block.id })
                }
                if (isEditor) {
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
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add New Block", color = MaterialTheme.colorScheme.secondary)
                        }
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
                                        "Delete Blog",
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
                                    text = "Blog Info",
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
                                        text = formatCustomDateTime(blog?.publishDate ?: "Unknown"),
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
                                    text = "Delete Blog?",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Are you sure you want to delete this Blog? This action cannot be undone.",
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
                    focusBlockId = blocks.getOrNull(index - 1)?.id
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
