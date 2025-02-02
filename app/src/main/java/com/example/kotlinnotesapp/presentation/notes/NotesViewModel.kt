package com.example.kotlinnotesapp.presentation.notes

import androidx.lifecycle.ViewModel
import com.example.kotlinnotesapp.data.model.Note

class NotesViewModel : ViewModel() {
    val notes = mutableListOf<Note>()

    fun addNote (note: Note) {
        notes.add(note)
    }

    fun deleteNote (note : Note) {
        notes.remove(note)
    }

    fun deleteAllNotes () {
        notes.clear()
    }
}
