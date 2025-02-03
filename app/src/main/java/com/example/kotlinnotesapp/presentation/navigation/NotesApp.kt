package com.example.kotlinnotesapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlinnotesapp.presentation.addnote.AddNoteScreen
import com.example.kotlinnotesapp.presentation.editnote.EditNoteScreen
import com.example.kotlinnotesapp.presentation.notes.NotesScreen
import com.example.kotlinnotesapp.presentation.notes.NotesViewModel

@Composable
fun NotesApp(
    notesViewModel: NotesViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

  NavHost(navController = navController, startDestination = "noteScreen") {
    composable("noteScreen") {
      NotesScreen(
          modifier = modifier,
          onNavigateToAddNote = { navController.navigate("addNoteScreen") },
          navHostController = navController,
          notesViewModel = notesViewModel,
      )
    }
    composable("addNoteScreen") {
      AddNoteScreen(
          navController = navController, modifier = modifier, notesViewModel = notesViewModel)
    }
    composable("edit_note/{noteId}") { backStackEntry ->
      val noteId = backStackEntry.arguments?.getString("noteId")
      val note = notesViewModel.notes.find { it.id == noteId?.toInt() }
      if (note != null) {
        EditNoteScreen(
            note = note,
            notesViewModel = notesViewModel,
            navController = navController,
            modifier = modifier,
        )
      }
    }
  }
}
