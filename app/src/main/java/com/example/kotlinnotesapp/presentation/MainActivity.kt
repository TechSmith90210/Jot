package com.example.kotlinnotesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kotlinnotesapp.data.db.NotesDatabase
import com.example.kotlinnotesapp.data.repository.NoteRepositoryImpl
import com.example.kotlinnotesapp.presentation.navigation.NotesApp
import com.example.kotlinnotesapp.presentation.notes.NotesViewModel
import com.example.kotlinnotesapp.presentation.theme.KotlinNotesAppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      KotlinNotesAppTheme {
        val database = NotesDatabase.getDatabase(applicationContext)
        val repository = NoteRepositoryImpl(database.noteDao())
        val viewModel = NotesViewModel(repository)
        val navController = rememberNavController()
        NotesApp(
            notesViewModel = viewModel,
            navController = navController,
            modifier = Modifier.fillMaxSize())
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun NotesAppPreview() {
  KotlinNotesAppTheme {
    val database = NotesDatabase.getDatabase(LocalContext.current)
    val repository = NoteRepositoryImpl(database.noteDao())
    val viewModel = NotesViewModel(repository)
    NotesApp(notesViewModel = viewModel, navController = rememberNavController())
  }
}
