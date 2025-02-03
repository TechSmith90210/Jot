package com.example.kotlinnotesapp.presentation.notes

import androidx.lifecycle.ViewModel
import com.example.kotlinnotesapp.data.model.Note

class NotesViewModel : ViewModel() {
    private val  _notes = mutableListOf<Note>()
    val notes : List<Note> get() = _notes

    fun addNote(note: Note) {
        val newId = note.id ?: if (_notes.isEmpty()) 1 else _notes.maxOf { it.id ?: 0 } + 1
        _notes.add(note.copy(id = newId))
    }

    fun deleteNote (note : Note) {
        _notes.remove(note)
    }

    fun updateNote (note : Note) {
        _notes.replaceAll { if (it.id == note.id) note else it }
    }
}
