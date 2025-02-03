package com.example.kotlinnotesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kotlinnotesapp.presentation.navigation.NotesApp
import com.example.kotlinnotesapp.presentation.notes.NotesViewModel
import com.example.kotlinnotesapp.presentation.theme.KotlinNotesAppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      KotlinNotesAppTheme {
        val navController = rememberNavController()
        val notesViewModel = NotesViewModel()
        NotesApp(
            notesViewModel = notesViewModel,
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
    NotesApp(notesViewModel = NotesViewModel(), navController = rememberNavController())
  }
}
