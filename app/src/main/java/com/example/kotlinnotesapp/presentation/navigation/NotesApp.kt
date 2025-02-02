package com.example.kotlinnotesapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlinnotesapp.presentation.addnote.AddNoteScreen
import com.example.kotlinnotesapp.presentation.notes.NotesScreen
import com.example.kotlinnotesapp.presentation.notes.NotesViewModel

@Composable
fun NotesApp (
    notesViewModel: NotesViewModel,
    navController : NavHostController,
    modifier: Modifier = Modifier) {

    NavHost(navController = navController, startDestination = "noteScreen") {
        composable("noteScreen") {
            NotesScreen(
                modifier = modifier,
                onDeleteAll = { notesViewModel.deleteAllNotes() },
                onNavigateToAddNote = { navController.navigate("addNoteScreen") },
                notesViewModel = notesViewModel,
                navController = navController,
            )
        }
        composable("addNoteScreen") {
            AddNoteScreen(
                navController = navController,
                modifier = modifier,
                notesViewModel = notesViewModel
            )
        }
    }
}