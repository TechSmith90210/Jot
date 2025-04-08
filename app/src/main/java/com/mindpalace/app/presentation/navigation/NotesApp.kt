package com.mindpalace.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.mindpalace.app.presentation.addnote.AddNoteScreen
import com.mindspace.app.presentation.editnote.EditNoteScreen
import com.mindspace.app.presentation.notes.NotesScreen
import com.mindspace.app.presentation.notes.NotesViewModel

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
        // case where note is not found
        print("NO NOTE FOUND")
      }
    }
  }
}
