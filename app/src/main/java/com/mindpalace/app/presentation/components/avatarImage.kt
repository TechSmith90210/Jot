package com.mindpalace.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder

@Composable
fun AvatarImage(randomSeed: String) {
    val randomAvatarLink = "https://api.dicebear.com/9.x/notionists/svg?seed=$randomSeed"
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    AsyncImage(
        model = randomAvatarLink,
        contentDescription = "Random Avatar",
        imageLoader = imageLoader,
        modifier = Modifier
            .size(135.dp)
            .clip(CircleShape)
            .background(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary
            )
    )

}