package com.example.kotlinnotesapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinnotesapp.data.model.Note
import com.example.kotlinnotesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel (private val repository: NoteRepository) : ViewModel() {
    private val  _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes : StateFlow<List<Note>> = _notes

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collect { fetchedNotes ->
                _notes.value = fetchedNotes
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote (note : Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun updateNote (note : Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
}
