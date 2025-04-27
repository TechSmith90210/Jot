package com.mindpalace.app.presentation.screens.blog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalace.app.R

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

                                BlogSmallPage(
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

@Composable
fun BlogSmallPage(
    title: String, description: String, author: String, date: String
) {
    Card(
        modifier = Modifier.fillMaxHeight(0.7f),
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,

            ),
        content = {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 8,
                    lineHeight = 25.sp
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = author,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }
        })
}

data class BlogPost(
    val title: String, val description: String, val author: String, val date: String
)

val pageContents = listOf(
    BlogPost(
        title = "10 Tips for Effective Time Management",
        description = "Once upon a time, in a land far, far away, there was a small village nestled between towering mountains and lush green forests. The village, called Solara, was known for its breathtaking views, vibrant flora, and the people who lived there. The villagers, humble and hardworking, lived simple but fulfilling lives, tending to their farms, raising animals, and crafting goods that they would trade with travelers passing through. They were a tight-knit community, bound by friendship, shared history, and a deep respect for nature.\n" + "\n" + "One day, a mysterious traveler arrived in Solara. He was dressed in tattered robes, his face partially covered by a hood, and he carried an ancient staff that seemed to glow with an eerie light. The villagers, curious but wary, watched him as he made his way toward the village center. He stopped at the fountain where the village elder, a wise old woman named Elysia, sat knitting a blanket.\n" + "\n" + "\"Greetings, traveler,\" Elysia said with a warm smile. \"What brings you to our peaceful village?\"\n" + "\n" + "The traveler looked at her with piercing eyes, his gaze intense but kind. \"I seek knowledge,\" he replied, his voice soft yet firm. \"I have traveled across many lands in search of something that has long been lost.\"\n" + "\n" + "Elysia raised an eyebrow, intrigued by his words. \"What is it that you seek, and why have you come to Solara?\"\n" + "\n" + "The traveler paused for a moment, as if weighing his words carefully. \"I am looking for the Heartstone, a powerful artifact that was said to have been hidden long ago by your ancestors. It is said to hold the key to unlocking untold knowledge, and with it, the power to shape the fate of the world.\"",
        author = "Jane Doe",
        date = "April 25, 2025"
    ), BlogPost(
        title = "The Future of Artificial Intelligence in Healthcare",
        description = "Artificial intelligence (AI) is revolutionizing the healthcare industry. From diagnosing diseases to predicting patient outcomes, AI has the potential to dramatically improve patient care and streamline processes. This article explores how AI will shape the future of healthcare.",
        author = "John Smith",
        date = "April 22, 2025"
    ), BlogPost(
        title = "How to Stay Motivated During Difficult Times",
        description = "Staying motivated during challenging times can be difficult, but it’s essential for mental well-being and success. This blog shares actionable strategies to help you overcome obstacles and stay focused, no matter the circumstances.",
        author = "Emily Carter",
        date = "April 18, 2025"
    ), BlogPost(
        title = "Exploring the Rise of Remote Work: Benefits and Challenges",
        description = "The shift to remote work has become a significant trend in the past few years, especially after the pandemic. This post discusses the benefits of remote work as well as the challenges that companies and employees face while adapting to this new normal.",
        author = "Michael Johnson",
        date = "April 15, 2025"
    ), BlogPost(
        title = "How Blockchain is Changing the Financial Industry",
        description = "Blockchain technology is disrupting the traditional financial industry. This article explains the basics of blockchain, how it works, and its implications for financial institutions and consumers. The decentralized nature of blockchain may reshape the future of banking.",
        author = "Sarah Lee",
        date = "April 12, 2025"
    ), BlogPost(
        title = "10 Must-Read Books for Personal Growth",
        description = "Books have the power to inspire and help individuals grow. In this post, we’ve compiled a list of 10 must-read books that will expand your mind, improve your skills, and help you live a more fulfilling life.",
        author = "David Wilson",
        date = "April 10, 2025"
    ), BlogPost(
        title = "Understanding Climate Change: Causes and Solutions",
        description = "Climate change is one of the most urgent issues facing the world today. This blog post delves into the science behind climate change, its effects on the environment, and what actions we can take to mitigate its impact.",
        author = "Sophia Brown",
        date = "April 5, 2025"
    ), BlogPost(
        title = "Top 5 Coding Languages Every Developer Should Learn in 2025",
        description = "As technology continues to evolve, developers need to stay updated with the latest programming languages. This post highlights the top 5 programming languages every developer should consider learning in 2025.",
        author = "Lucas Martinez",
        date = "April 1, 2025"
    ), BlogPost(
        title = "Mental Health Awareness: Breaking the Stigma",
        description = "Mental health is just as important as physical health, yet it is often overlooked. In this article, we’ll discuss the stigma surrounding mental health, the importance of seeking help, and ways to promote mental well-being in society.",
        author = "Olivia White",
        date = "March 30, 2025"
    ), BlogPost(
        title = "How to Build a Sustainable Garden in Your Backyard",
        description = "Sustainability is more important than ever, and one way you can contribute is by building a sustainable garden. This post will guide you through the steps to create a garden that’s eco-friendly, low-maintenance, and beneficial to the environment.",
        author = "Aaron Green",
        date = "March 25, 2025"
    )
)
