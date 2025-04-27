package com.mindpalace.app.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.PageListItem
import com.mindpalace.app.presentation.components.RecentNotesCard
import com.mindpalace.app.presentation.components.titles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Mind Palace", style = MaterialTheme.typography.titleSmall) },
                actions = {
                    IconButton(
                        onClick = { },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.upload_cloud_line),
                                contentDescription = "upload status",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                },
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
                            Column(
                                content = {
                                    titles.forEachIndexed { index, title ->
                                        ListItem(
                                            modifier = Modifier.clickable {},
                                            headlineContent = {
                                                Text(
                                                    title,
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
                                        if (index != titles.lastIndex + 1) {
                                            Modifier.padding(horizontal = 16.dp)
                                            HorizontalDivider(
                                                thickness = 1.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                modifier = Modifier.padding(start = 58.dp)
                                            )
                                        }
                                        if (index == titles.lastIndex) {
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
                                    }
                                })
                        })
                })
        })
}






