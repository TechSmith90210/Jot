package com.mindpalace.app.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R
import com.mindpalace.app.presentation.screens.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier, onNavigateBack : () -> Unit) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings", style = MaterialTheme.typography.titleSmall)
                },
                navigationIcon = {
                   IconButton(onClick = {
                       onNavigateBack()
                   }){ Icon(
                       painter = painterResource(id = R.drawable.arrow_left_line),
                       contentDescription = ""
                   )}
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
                        Text("Settings Screen")
                    }
                })
        })
}