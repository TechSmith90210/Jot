package com.example.kotlinnotesapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinnotesapp.ui.screens.AddNoteScreen
import com.example.kotlinnotesapp.ui.screens.NotesScreen

@Composable
fun NotesApp (modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var notes = remember { mutableStateListOf<Note>() }

    NavHost(navController = navController, startDestination = "noteScreen") {
        composable("noteScreen") {
            NotesScreen(notes = notes,
                modifier = modifier,
                onDeleteAll = {notes.clear()},
                onNavigateToAddNote = { navController.navigate("addNoteScreen") },
            )
        }
        composable("addNoteScreen") {
            AddNoteScreen(
                onNoteAdd = { note ->
                    notes.add(Note(title = note.title, body = note.body))
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}