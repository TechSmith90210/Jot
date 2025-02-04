package com.example.kotlinnotesapp.domain.usecase

import com.example.kotlinnotesapp.domain.repository.NoteRepository
import com.example.kotlinnotesapp.data.model.Note

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note)
    }
}
