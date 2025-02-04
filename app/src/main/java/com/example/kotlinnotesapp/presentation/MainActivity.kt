package com.example.kotlinnotesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.kotlinnotesapp.data.db.NotesDatabase
import com.example.kotlinnotesapp.data.repository.NoteRepositoryImpl
import com.example.kotlinnotesapp.domain.usecase.DeleteNoteUseCase
import com.example.kotlinnotesapp.domain.usecase.GetNotesUseCase
import com.example.kotlinnotesapp.domain.usecase.InsertNoteUseCase
import com.example.kotlinnotesapp.domain.usecase.NoteUseCases
import com.example.kotlinnotesapp.domain.usecase.UpdateNoteUseCase
import com.example.kotlinnotesapp.presentation.navigation.NotesApp
import com.example.kotlinnotesapp.presentation.notes.NotesViewModel
import com.example.kotlinnotesapp.presentation.theme.KotlinNotesAppTheme

class MainActivity : ComponentActivity() {

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      KotlinNotesAppTheme {
        // Initializing the database and repository
        val database = NotesDatabase.getDatabase(applicationContext)
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
            notesViewModel = viewModel,
            navController = navController,
            modifier = Modifier.fillMaxSize())
      }
    }
  }
}
