package com.example.kotlinnotesapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinnotesapp.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen (
    onNoteAdd : (Note) -> Unit,
    onBack : () -> Unit,
    modifier: Modifier
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    Scaffold (
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Add Note", color = Color.Black, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        //delete all notes
                        onBack()
                    },) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Delete All Notes",
                            tint = Color.Black,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = {
                if(title.isNotEmpty() && body.isNotEmpty()){
                    onNoteAdd(Note(title = title, body = body)) // Add a new note
                } else {
                    println("Title or body is empty")
                }
            },
                containerColor = Color.Blue
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Add Note",
                    tint = Color.White,
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart // Align to the start of the box
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {title = it},
                    placeholder = { Text(text = "Enter Title") },
                    label = { Text(text = "Title") },
                    modifier = Modifier.fillMaxWidth(1f) // Use 80% of the width
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                OutlinedTextField(
                    value = body,
                    onValueChange = {body = it},
                    placeholder = { Text(text = "Enter Body") },
                    label = { Text(text = "Body") },
                    modifier = Modifier.fillMaxWidth(1f) // Use 80% of the width
                )
            }
        }

    }
}

@Preview
@Composable
fun AddNoteScreenPreview() {
    AddNoteScreen(onNoteAdd = {}, onBack = {}, modifier = Modifier)
}