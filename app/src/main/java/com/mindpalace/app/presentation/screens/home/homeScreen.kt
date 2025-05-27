package com.mindpalace.app.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.presentation.components.LoadingScreen
import com.mindpalace.app.presentation.components.PageListItem
import com.mindpalace.app.presentation.components.RecentNotesCard
import com.mindpalace.app.presentation.components.titles
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentState
import com.mindpalace.app.presentation.screens.mind_fragment.MindFragmentViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mindFragmentViewModel: MindFragmentViewModel = hiltViewModel(),
    onFragmentClick: (String) -> Unit
) {
    val state by mindFragmentViewModel.state.collectAsState()
    val auth: Auth = SupabaseClient.client.auth
    LaunchedEffect(Unit) {
        val currentUserId = auth.currentUserOrNull()?.id
        mindFragmentViewModel.getFragmentsByCreatedAt(userId = currentUserId.toString(), limit = 10)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Mind Palace", style = MaterialTheme.typography.titleSmall) },
                expandedHeight = 40.dp,
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        content = { padding ->
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(padding),
                content = {
                    Spacer(Modifier.height(7.dp))
                    Text(
                        text = "Jump Back In",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        flingBehavior = ScrollableDefaults.flingBehavior(),
                        horizontalArrangement = Arrangement.spacedBy(13.dp),
                        content = {
                            item {
                                Spacer(modifier = Modifier.width(0.dp))  // This creates space on the left
                            }
                            items(count = titles.size, itemContent = {
                                RecentNotesCard(title = titles[it])
                            })
                            item {
                                Spacer(modifier = Modifier.width(0.dp))  // This creates space on the left
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PageListItem(title = "Reminders")
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Text(
                                text = "Pages",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.more_line),
                                    contentDescription = "more",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable(
                                            onClick = { println("Hello u clicked me") },
                                            enabled = true,
                                            onClickLabel = null,
                                            role = null,
                                            interactionSource = null,
                                            indication = null
                                        )
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.add_fill),
                                    contentDescription = "add page",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable(
                                            onClick = { println("Hello u clicked me") },
                                            enabled = true,
                                            onClickLabel = null,
                                            role = null,
                                            interactionSource = null,
                                            indication = null
                                        )
                                )
                            }

                        })
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        content = {
                            when (state) {
                                is MindFragmentState.SuccessList -> {
                                    val fragments =
                                        (state as MindFragmentState.SuccessList).summaryList

                                    if (fragments.isNullOrEmpty()) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 24.dp, vertical = 48.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.undraw_add_notes_9xls),
                                                contentDescription = "No notes image",
                                                modifier = Modifier
                                                    .fillMaxWidth(0.6f)
                                                    .aspectRatio(1.2f)
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))

                                            Text(
                                                text = "No Mind Fragments Yet",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = "Tap below to create your first mind fragment and start organizing your thoughts.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.secondary,
                                                textAlign = TextAlign.Center
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))

                                            Button(
                                                onClick = { /* TODO: Navigate to fragment editor */ },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .height(40.dp)
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Create Fragment",
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }



                                } else {
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            items(fragments.size) { index ->
                                                val fragment = fragments[index]
                                                ListItem(
                                                    modifier = Modifier
                                                        .clickable {
                                                            onFragmentClick(fragment.id)
                                                        },
                                                    headlineContent = {
                                                        Text(
                                                            fragment.title,
                                                            style = MaterialTheme.typography.labelLarge,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                    },
                                                    trailingContent = {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.arrow_right_s_line),
                                                            contentDescription = "arrow",
                                                            tint = MaterialTheme.colorScheme.secondary
                                                        )
                                                    },
                                                    leadingContent = {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.file_text_line),
                                                            contentDescription = "file icon",
                                                            tint = MaterialTheme.colorScheme.secondary
                                                        )
                                                    },
                                                    colors = ListItemDefaults.colors(
                                                        containerColor = MaterialTheme.colorScheme.surface,
                                                        headlineColor = MaterialTheme.colorScheme.onSurface,
                                                        trailingIconColor = MaterialTheme.colorScheme.onSurface
                                                    )
                                                )

                                                if (index != fragments.lastIndex + 1) {
                                                    Modifier.padding(horizontal = 16.dp)
                                                    HorizontalDivider(
                                                        thickness = 1.dp,
                                                        color = MaterialTheme.colorScheme.outline,
                                                        modifier = Modifier.padding(start = 58.dp)
                                                    )
                                                }
                                                if (index == fragments.lastIndex) {
                                                    ListItem(
                                                        modifier = Modifier.clickable {},
                                                        leadingContent = {
                                                            Icon(
                                                                painter = painterResource(id = R.drawable.more_line),
                                                                contentDescription = ""
                                                            )
                                                        },
                                                        headlineContent = {
                                                            Text(
                                                                "View More",
                                                                style = MaterialTheme.typography.labelLarge
                                                            )
                                                        })
                                                }
                                                HorizontalDivider(
                                                    thickness = 1.dp,
                                                    color = MaterialTheme.colorScheme.outline,
                                                    modifier = Modifier.padding(start = 58.dp)
                                                )
                                            }
                                        }
                                    }


                                }

                                is MindFragmentState.Loading -> {
                                    LoadingScreen()
                                }

                                is MindFragmentState.Error -> {
                                    Text(text = "Error : ${(state as MindFragmentState.Error).message}")
                                }

                                else -> {}
                            }
                        })
                })
        })
}