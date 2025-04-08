package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.repository.NoteRepository
import com.mindpalace.app.data.model.Note

class InsertNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}
