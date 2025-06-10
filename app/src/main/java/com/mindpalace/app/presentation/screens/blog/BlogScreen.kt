package com.mindpalace.app.presentation.screens.blog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AvatarImage
import com.mindpalace.app.presentation.components.BlogCard
import com.mindpalace.app.presentation.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    modifier: Modifier = Modifier,
    onCreateBlogClick: () -> Unit,
    blogViewModel: BlogViewModel = hiltViewModel(),
    onBlogCLick: (String) -> Unit
) {
    val state by blogViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        blogViewModel.loadBlogs()
    }

    when (state) {
        is BlogState.Loading -> {
            LoadingScreen()
        }

        is BlogState.Error -> {
            Text(text = "Error: ${(state as BlogState.Error).message}")
        }

        is BlogState.SuccessList -> {

            val allBlogs = (state as BlogState.SuccessList).allBlogs
            val latestBlogs = (state as BlogState.SuccessList).latestBlogs
            val pagerState = rememberPagerState(pageCount = { latestBlogs?.size ?: 0 })

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Mind Blogs", style = MaterialTheme.typography.titleSmall
                            )
                        },
                        actions = {
                            IconButton(onClick = { onCreateBlogClick() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sticky_note_add_line),
                                    contentDescription = "Add Blog",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                            actionIconContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            ) { padding ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        Text(
                            text = "Explore the new mind blogs",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    item {
                        HorizontalPager(
                            state = pagerState,
                            pageSize = PageSize.Fixed(300.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            pageSpacing = 5.dp,
                        ) { index ->
                            val blogPost = latestBlogs?.get(index)
                            if (blogPost != null)
                                BlogCard(
                                    title = blogPost.title,
                                    description = blogPost.description,
                                    author = blogPost.user.displayName,
                                    date = blogPost.publishDate,
                                    onBlogClick = {
                                        onBlogCLick(blogPost.id)
                                    }
                                )
                        }
                    }

                    item {
                        Text(
                            text = "Other Mind Blogs",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    items(allBlogs?.size ?: 0) { index ->
                        val blog = allBlogs?.get(index) ?: return@items
                        ListItem(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).clickable {
                                onBlogCLick(blog.id)
                            },

                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.background,
                                headlineColor = MaterialTheme.colorScheme.onBackground,
                                leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                overlineColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            headlineContent = {
                                Column {
                                    Text(
                                        text = blog.title,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "by ${blog.user.displayName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            supportingContent = {
                                Text(
                                    text = blog.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    modifier = Modifier.padding(top = 8.dp),
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingContent = {
                                Box(
                                    modifier = Modifier.size(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AvatarImage(
                                        randomSeed = blog.user.avatarId
                                    )
                                }

                            },
                        )
                    }

                }
            }
        }

        else -> {}

    }


}

