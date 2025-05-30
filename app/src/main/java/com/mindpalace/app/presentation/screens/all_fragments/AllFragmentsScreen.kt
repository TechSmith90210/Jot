@file:OptIn(ExperimentalMaterial3Api::class)

package com.mindpalace.app.presentation.screens.all_fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.core.highlightQueryInTitle
import com.mindpalace.app.presentation.components.LoadingScreen
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentState
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth

@Composable
fun AllFragmentsScreen(
    mindFragmentViewModel: MindFragmentViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onFragmentClick: (String) -> Unit,
    onCreateFragmentClick: () -> Unit,
) {
    val state by mindFragmentViewModel.state.collectAsState()
    val auth: Auth = SupabaseClient.client.auth
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        val currentUserId = auth.currentUserOrNull()?.id
        mindFragmentViewModel.getAllFragments(userId = currentUserId.toString())
    }

    when (state) {
        is MindFragmentState.Loading -> {
            LoadingScreen()
        }

        is MindFragmentState.Error -> {
            Text(text = "Error: ${(state as MindFragmentState.Error).message}")
        }

        is MindFragmentState.SuccessList -> {
            val allFragments = (state as MindFragmentState.SuccessList).createdList
            val searchedFragments = remember(searchQuery.value.text, allFragments) {
                val query = searchQuery.value.text.trim()
                if (query.isBlank()) allFragments
                else allFragments?.filter {
                    it.title.contains(query, ignoreCase = true) == true
                }
            }

            if (allFragments?.isEmpty() ?: return) {
                Surface {
                    Text("No Fragments Yet")
                }
            } else {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            title = {
                                TextField(
                                    value = searchQuery.value,
                                    onValueChange = { searchQuery.value = it },
                                    singleLine = true,
                                    maxLines = 1,
                                    shape = RoundedCornerShape(15.dp),
                                    placeholder = {
                                        Text(
                                            text = "Search For Notes or Blogs ...",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(15.dp)
                                        ),
                                    textStyle = MaterialTheme.typography.titleSmall,
                                    keyboardActions = KeyboardActions(
                                        onDone = { focusManager.clearFocus() }
                                    ),
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.search_line),
                                            contentDescription = "Search icon",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.background,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                        cursorColor = MaterialTheme.colorScheme.onSurface,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { onNavigateBack() },
                                    content = {
                                        Icon(
                                            painter = painterResource(R.drawable.arrow_left_line),
                                            contentDescription = "back",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                )
                            }
                        )
                    },
                    content = { padding ->
                        LazyColumn(
                            contentPadding = PaddingValues(8.dp),
                            state = rememberLazyListState(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(padding)
                                .padding(horizontal = 8.dp),
                        ) {
                            items(searchedFragments?.size ?: 0) { index ->
                                val item = searchedFragments?.get(index) ?: return@items

                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = {
                                        if (it == SwipeToDismissBoxValue.EndToStart) {
                                            mindFragmentViewModel.deleteFragment(
                                                auth.currentUserOrNull()?.id.toString(),
                                                item.id
                                            )
                                            onNavigateBack()
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                )
                                SwipeToDismissBox(
                                    content = {
                                        Surface(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            ListItem(
                                                colors = ListItemDefaults.colors(
                                                    containerColor = Color.Transparent,
                                                    headlineColor = MaterialTheme.colorScheme.onSurface,
                                                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                                                ),
                                                modifier = Modifier
                                                    .border(
                                                        width = 1.dp,
                                                        color = MaterialTheme.colorScheme.outline,
                                                        shape = MaterialTheme.shapes.medium
                                                    )
                                                    .clickable {
                                                        onFragmentClick(item.id)
                                                    },
                                                leadingContent = {
                                                    Icon(
                                                        painter = painterResource(R.drawable.file_text_line),
                                                        contentDescription = "file icon",
                                                    )
                                                },
                                                headlineContent = {
                                                    Text(
                                                        highlightQueryInTitle(
                                                            item.title,
                                                            searchQuery.value.text.trim()
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    },
                                    state = dismissState,
                                    backgroundContent = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape = MaterialTheme.shapes.medium
                                                )
                                                .background(
                                                    color = MaterialTheme.colorScheme.error.copy(
                                                        alpha = 0.08f
                                                    ),
                                                    shape = MaterialTheme.shapes.medium
                                                )
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.delete_bin_6_line),
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    },

                                    enableDismissFromStartToEnd = false,
                                    enableDismissFromEndToStart = true,
                                    gesturesEnabled = true
                                )


                            }

                        }

                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { onCreateFragmentClick() }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_fill),
                                contentDescription = "Add Note"
                            )
                        }
                    }
                )
            }
        }

        else -> {}
    }


}
