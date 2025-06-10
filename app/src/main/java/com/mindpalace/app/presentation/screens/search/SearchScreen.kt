package com.mindpalace.app.presentation.screens.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    Scaffold(
        content = {  paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxHeight().padding(paddingValues).clickable(
                        indication = null,
                        interactionSource = null

                    ){
                        focusManager.clearFocus()
                    },
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search bar at the bottom
                TextField(
                    value = searchQuery.value,
                    onValueChange = { newValue -> searchQuery.value = newValue },
                    label = { Text(text = "Search For Notes or Blogs ...",style = MaterialTheme.typography.labelSmall) },
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 5.dp).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(15.dp)),
                    textStyle = MaterialTheme.typography.headlineSmall,
                    keyboardActions = KeyboardActions (
                        onDone = {focusManager.clearFocus() },
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.search_line),
                            contentDescription = "",
                            tint=MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        })
}
