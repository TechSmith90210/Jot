package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.repository.NoteRepository
import com.mindpalace.app.data.model.Note
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase (private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}
