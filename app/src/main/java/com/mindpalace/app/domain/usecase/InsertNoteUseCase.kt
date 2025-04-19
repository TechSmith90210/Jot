package com.mindpalace.app.domain.usecase

import com.mindpalace.app.data.model.Note
import com.mindpalace.app.domain.repository.NoteRepository

class InsertNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}
