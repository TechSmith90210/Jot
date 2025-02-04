package com.example.kotlinnotesapp.domain.usecase

import com.example.kotlinnotesapp.domain.repository.NoteRepository
import com.example.kotlinnotesapp.data.model.Note

class InsertNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}
