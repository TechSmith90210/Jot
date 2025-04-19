package com.mindpalace.app.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(modifier: Modifier) {
    Scaffold(
        content = { padding ->
            Box(
                modifier = modifier.padding(padding),
                content = {
                    Text("Home Screen")
                }
            )
        }
    )
}