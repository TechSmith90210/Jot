package com.example.kotlinnotesapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinnotesapp.data.model.Note
import com.example.kotlinnotesapp.domain.usecase.NoteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val noteUseCases: NoteUseCases) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteUseCases.getNotes().collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.updateNote(note)
        }
    }
}
