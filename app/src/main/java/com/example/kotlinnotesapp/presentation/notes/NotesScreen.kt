package com.example.kotlinnotesapp.presentation.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinnotesapp.data.db.NotesDatabase
import com.example.kotlinnotesapp.data.model.Note
import com.example.kotlinnotesapp.data.repository.NoteRepositoryImpl
import com.example.kotlinnotesapp.presentation.components.NoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel,
    onNavigateToAddNote: () -> Unit,
    onNavigateToEditNote: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "notes",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = { onNavigateToAddNote() },
                containerColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = Color.White,
                )
            }
        }
    ) { paddingValues ->
        NotesList(
            notes = notesViewModel.notes.collectAsState().value,
            onDelete = { note -> notesViewModel.deleteNote(note) },
            modifier = modifier,
            onEdit = { note -> onNavigateToEditNote(note.id) },
            paddingValues = paddingValues
        )
    }
}


@Composable
fun NotesList(
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    onEdit: (Note) -> Unit,
    modifier: Modifier,
    paddingValues: PaddingValues
) {
  LazyColumn(modifier = modifier
      .fillMaxSize()
      .padding(paddingValues)
      .padding(horizontal = 5.dp)) {
    items(notes, key = { note -> note.id }) { note ->
      val dismissState =
          rememberSwipeToDismissBoxState(
              confirmValueChange = { dismissValue ->
                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                  onDelete(note)
                  true
                } else false
              })

      SwipeToDismissBox(
          state = dismissState,
          backgroundContent = {},
          content = {
              Box(
                  modifier = modifier
                      .clickable(
                          indication = null,
                          interactionSource = remember { MutableInteractionSource() },
                          onClick = { onEdit(note) }
                      )
              ) {
                  NoteCard(title = note.title, body = note.body)
              }

          })
    }
  }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=440,cutout=punch_hole,navigation=buttons")
@Composable
fun NotesScreenPreview() {
    val database = NotesDatabase.getDatabase(LocalContext.current)
    val repository = NoteRepositoryImpl(database.noteDao())
    val viewModel = NotesViewModel(repository)

  NotesScreen(
      notesViewModel = viewModel,
      onNavigateToAddNote = {},
      modifier = Modifier,
      onNavigateToEditNote = {  }
  )
}
