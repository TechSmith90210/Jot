package com.mindpalace.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalace.app.core.formatCustomDateTime

@Composable
fun BlogCard(
    title: String, description: String, author: String, date: String
) {
    Card(
        onClick = { /* TODO: Handle blog click */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 26.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.size(15.dp)) { AvatarImage(author) }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = author, style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    text = formatCustomDateTime(date) , style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


data class BlogPost(
    val title: String, val description: String, val author: String, val date: String
)

val pageContents = listOf(
    BlogPost(
        title = "10 Tips for Effective Time Management",
        description = "Master your day with time-blocking, prioritization, and digital tools that boost focus and productivity.",
        author = "purplefish404",
        date = "April 25, 2025"
    ), BlogPost(
        title = "The Future of Artificial Intelligence in Healthcare",
        description = "AI is reshaping healthcare through early diagnosis, predictive analytics, and personalized treatment.",
        author = "organicleopard226",
        date = "April 22, 2025"
    ), BlogPost(
        title = "How to Stay Motivated During Difficult Times",
        description = "Resilience starts with small goals, gratitude, and support. Turn setbacks into powerful comebacks.",
        author = "goldenrabbit314",
        date = "April 18, 2025"
    ), BlogPost(
        title = "Exploring the Rise of Remote Work: Benefits and Challenges",
        description = "Remote work offers freedom and balance—but also demands new ways to stay connected and focused.",
        author = "tinygoose389",
        date = "April 15, 2025"
    )
)