package com.example.kotlinnotesapp.presentation.editnote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kotlinnotesapp.data.db.NotesDatabase
import com.example.kotlinnotesapp.data.model.Note
import com.example.kotlinnotesapp.data.repository.NoteRepositoryImpl
import com.example.kotlinnotesapp.domain.usecase.DeleteNoteUseCase
import com.example.kotlinnotesapp.domain.usecase.GetNotesUseCase
import com.example.kotlinnotesapp.domain.usecase.InsertNoteUseCase
import com.example.kotlinnotesapp.domain.usecase.NoteUseCases
import com.example.kotlinnotesapp.domain.usecase.UpdateNoteUseCase
import com.example.kotlinnotesapp.presentation.notes.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    note: Note, // The existing note to be edited
    notesViewModel: NotesViewModel,
    navController: NavHostController,
    modifier: Modifier
) {
  var title by remember { mutableStateOf(note.title) }
  var body by remember { mutableStateOf(note.body) }

  Scaffold(
      containerColor = Color.White,
      modifier = modifier.fillMaxSize(),
      topBar = {
        CenterAlignedTopAppBar(
            title = {
              Text(
                  text = "Edit Note",
                  color = Color.Black,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
              )
            },
            colors =
                TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
            navigationIcon = {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Go Back",
                  tint = Color.Black,
                  modifier =
                      Modifier.size(30.dp).padding(start = 10.dp).clickable {
                        navController.popBackStack() // Navigate back
                      })
            })
      },
      floatingActionButton = {
        SmallFloatingActionButton(
            onClick = {
              if (title.isNotEmpty() && body.isNotEmpty()) {
                // Update the existing note
                notesViewModel.updateNote(note.copy(title = title, body = body))
                navController.popBackStack() // Navigate back after saving
              } else {
                println("Title or body is empty")
              }
            },
            containerColor = Color.Blue) {
              Icon(
                  imageVector = Icons.Filled.Check,
                  contentDescription = "Save Changes",
                  tint = Color.White,
              )
            }
      }) { paddingValues ->
        Column(modifier = modifier.fillMaxSize().padding(paddingValues).padding(20.dp)) {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions =
                    KeyboardActions(onNext = { defaultKeyboardAction(ImeAction.Next) }),
                textStyle =
                    TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                  if (title.isEmpty()) {
                    Text(
                        text = "Enter Title",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray)
                  }
                  innerTextField()
                })
          }

          Spacer(modifier = Modifier.height(16.dp))

          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            BasicTextField(
                value = body,
                onValueChange = { body = it },
                modifier = Modifier.fillMaxHeight(1f),
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                keyboardActions =
                    KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
                decorationBox = { innerTextField ->
                  if (body.isEmpty()) {
                    Text("Enter Body", fontSize = 18.sp, color = Color.Gray)
                  }
                  innerTextField()
                })
          }
        }
      }
}

@Preview
@Composable
fun EditNoteScreenPreview() {
    val database = NotesDatabase.getDatabase(LocalContext.current)
    val repository = NoteRepositoryImpl(database.noteDao())

    // Create NoteUseCases object directly and pass to ViewModel
    val noteUseCases = NoteUseCases(
        getNotes = GetNotesUseCase(repository),
        insertNote = InsertNoteUseCase(repository),
        deleteNote = DeleteNoteUseCase(repository),
        updateNote = UpdateNoteUseCase(repository)
    )

    // Passing the NoteUseCases to the ViewModel
    val viewModel = NotesViewModel(noteUseCases)

    EditNoteScreen(
      note = Note(id = 1, title = "Sample Note", body = "This is a note body."),
      notesViewModel = viewModel,
      navController = NavHostController(LocalContext.current),
      modifier = Modifier)
}
