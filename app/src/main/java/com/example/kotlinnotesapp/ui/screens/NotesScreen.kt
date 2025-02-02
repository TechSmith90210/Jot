package com.example.kotlinnotesapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinnotesapp.Note
import com.example.kotlinnotesapp.ui.components.NoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen (notes : MutableList<Note>,
                 onDeleteAll : () -> Unit,
                 onNavigateToAddNote : () -> Unit,
                 modifier: Modifier ) {
    Scaffold (
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Notes", color = Color.Black, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = {
                        //delete all notes
                        onDeleteAll()
                    },) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete All Notes",
                            tint = Color.Black,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = {
                onNavigateToAddNote()
            },
                containerColor = Color.Black
            ) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = Color.White,
                )
            }
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)) {
            items(notes) {
                    note ->
                NoteCard(title = note.title, body = note.body)
            }
        }
    }
}