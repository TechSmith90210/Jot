package com.example.kotlinnotesapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

  val notes = notesViewModel.notes.collectAsState().value

  NavHost(navController = navController, startDestination = "noteScreen") {
    composable("noteScreen") {
      NotesScreen(
          modifier = modifier,
          onNavigateToAddNote = { navController.navigate("addNoteScreen") },
          onNavigateToEditNote = { noteId -> navController.navigate("editNote/$noteId") },
          notesViewModel = notesViewModel,
      )
    }
    composable("addNoteScreen") {
      AddNoteScreen(
          navController = navController, modifier = modifier, notesViewModel = notesViewModel)
    }
    composable("editNote/{noteId}") { backStackEntry ->
      val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
      val note = notes.find { it.id == noteId }

      if (note != null) {
        EditNoteScreen(
            note = note,
            notesViewModel = notesViewModel,
            navController = navController,
            modifier = modifier,
        )
      } else {
        // Handle case where note is not found, perhaps navigate back or show an error message
        print("NO NOTE FOUND")
      }
    }
  }
}
