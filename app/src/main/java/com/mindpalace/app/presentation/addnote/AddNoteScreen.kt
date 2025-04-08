package com.mindpalace.app.presentation.addnote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.data.db.NotesDatabase
import com.mindpalace.app.data.model.Note
import com.mindpalace.app.data.repository.NoteRepositoryImpl
import com.mindpalace.app.domain.usecase.DeleteNoteUseCase
import com.mindpalace.app.domain.usecase.GetNotesUseCase
import com.mindpalace.app.domain.usecase.InsertNoteUseCase
import com.mindpalace.app.domain.usecase.NoteUseCases
import com.mindpalace.app.domain.usecase.UpdateNoteUseCase
import com.mindpalace.app.presentation.navigation.NotesApp
import com.mindspace.app.presentation.notes.NotesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    notesViewModel: NotesViewModel,
    navController: NavHostController,
    modifier: Modifier
) {
  var title by remember { mutableStateOf("") }
  var body by remember { mutableStateOf("") }
  var snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  fun showSnackbar(message: String) {
    scope.launch {
      snackbarHostState.showSnackbar(
          message = message, withDismissAction = true, duration = SnackbarDuration.Short)
    }
  }

  Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
      snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
              Snackbar(
                  snackbarData = data,
                  containerColor = Color.Black,
                  contentColor = Color.White,
                  dismissActionContentColor = Color.White)
            })
      },
      modifier = modifier.fillMaxSize(),
      topBar = {
        CenterAlignedTopAppBar(
            title = {
              Text(
                  text = "Add Note",
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
                        navController.popBackStack()
                      })
            })
      },
      floatingActionButton = {
        SmallFloatingActionButton(
            onClick = {
              when {
                title.isEmpty() && body.isEmpty() -> showSnackbar("Title and body cannot be empty")
                title.isEmpty() -> showSnackbar("Title cannot be empty")
                body.isEmpty() -> showSnackbar("Body cannot be empty")

                else -> {
                  notesViewModel.addNote(Note(title = title, body = body)) // Add a new note
                  navController.popBackStack() // Navigate back
                }
              }
            },
            containerColor = Color.Blue) {
              Icon(
                  imageVector = Icons.Filled.Check,
                  contentDescription = "Add Note",
                  tint = Color.White,
              )
            }
      }) { paddingValues ->
        Column(modifier = modifier.fillMaxSize().padding(paddingValues).padding(20.dp)) {
          Box(
              modifier = Modifier.fillMaxWidth(), // Adds spacing
              contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions =
                        KeyboardOptions(imeAction = ImeAction.Next), // Correct ImeAction
                    keyboardActions =
                        KeyboardActions(onNext = { defaultKeyboardAction(ImeAction.Next) }),
                    textStyle =
                        TextStyle(
                            fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black),
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
                    KeyboardActions {
                      defaultKeyboardAction(
                          imeAction = ImeAction.Done,
                      )
                    },
                decorationBox = { innerTextField ->
                  Column {
                    // Placeholder logic
                    Box {
                      if (body.isEmpty()) {
                        Text("Enter Body", fontSize = 18.sp, color = Color.Gray)
                      }
                      innerTextField()
                    }
                  }
                })
          }
        }
      }
}

@Preview
@Composable
fun AddNoteScreenPreview() {
  val database = NotesDatabase.getDatabase(LocalContext.current)
  val repository = NoteRepositoryImpl(database.noteDao())

  // Create NoteUseCases object directly and pass to ViewModel
  val noteUseCases =
      NoteUseCases(
          getNotes = GetNotesUseCase(repository),
          insertNote = InsertNoteUseCase(repository),
          deleteNote = DeleteNoteUseCase(repository),
          updateNote = UpdateNoteUseCase(repository))

  // Passing the NoteUseCases to the ViewModel
  val viewModel = NotesViewModel(noteUseCases)

  // Setting up the NavController
  val navController = rememberNavController()

  // Passing everything into the NotesApp composable
  NotesApp(
      notesViewModel = viewModel, navController = navController, modifier = Modifier.fillMaxSize())
}
