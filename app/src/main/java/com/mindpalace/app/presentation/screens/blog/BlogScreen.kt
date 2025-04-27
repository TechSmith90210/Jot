package com.mindpalace.app.presentation.screens.blog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.mindpalace.app.presentation.components.BlogCard
import com.mindpalace.app.presentation.components.pageContents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(modifier: Modifier) {
    val pagerState = rememberPagerState(
        pageCount = { pageContents.size })
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blogs", style = MaterialTheme.typography.titleSmall) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.sticky_note_add_line),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = "Search"
                        )
                    }
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
            Box(
                modifier = modifier.padding(padding),
                contentAlignment = Alignment.Center,
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            beyondViewportPageCount = 1,
                            pageSize = PageSize.Fixed(300.dp),
                            contentPadding = PaddingValues(25.dp),
                            pageSpacing = 10.dp,
                            pageContent = { page ->
                                val blogPost = pageContents[page]

                                BlogCard(
                                    title = blogPost.title,
                                    description = blogPost.description,
                                    author = blogPost.author,
                                    date = blogPost.date
                                )
                            })
                    }
                })
        })
}

