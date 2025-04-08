package com.mindpalace.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.presentation.navigation.NotesApp
import com.mindspace.app.presentation.notes.NotesViewModel
import com.mindpalace.app.presentation.theme.MindPalaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        MindPalaceTheme {
            val notesViewModel = hiltViewModel<NotesViewModel>()

            // Setting up the NavController
            val navController = rememberNavController()

            // Passing everything into the NotesApp composable
            NotesApp(
              notesViewModel = notesViewModel,
              navController = navController,
              modifier = Modifier.fillMaxSize())
        }
    }
  }
}
