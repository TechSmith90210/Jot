package com.example.kotlinnotesapp.domain.usecase

import com.example.kotlinnotesapp.domain.repository.NoteRepository
import com.example.kotlinnotesapp.data.model.Note
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}
