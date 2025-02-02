package com.example.kotlinnotesapp.presentation.notes

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlinnotesapp.data.model.Note
import com.example.kotlinnotesapp.presentation.components.NoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen (
                notesViewModel: NotesViewModel,
                navController: NavHostController,
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
                    },
                        ) {
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
    ) {
        paddingValues -> NotesList(
        notes = notesViewModel.notes,
        onDelete = { note -> notesViewModel.deleteNote(note) },
        modifier = modifier,
        paddingValues = paddingValues)
    }
}
@Composable
fun NotesList(notes: MutableList<Note>, onDelete: (Note) -> Unit, modifier: Modifier, paddingValues: PaddingValues) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 8.dp)) {
        items(notes) {
                note ->

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    dismissValue -> if( dismissValue == SwipeToDismissBoxValue.EndToStart) {
                        onDelete(note)
                    true
                }else false
                }
            )

            SwipeToDismissBox(state = dismissState, backgroundContent = {

            },
                content = {
                    NoteCard(title = note.title, body = note.body)
                }
                )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    val notesViewModel = NotesViewModel().apply {
        addNote(Note(title = "Sample Note", body = "This is a sample body"))
        addNote(Note(title = "Another Note", body = "This is another note"))
    }

    NotesScreen(
        notesViewModel = notesViewModel,
        navController = rememberNavController(),
        onDeleteAll = { notesViewModel.deleteAllNotes() },
        onNavigateToAddNote = { /* Navigate to Add Note Screen */ },
        modifier = TODO()
    )
}

