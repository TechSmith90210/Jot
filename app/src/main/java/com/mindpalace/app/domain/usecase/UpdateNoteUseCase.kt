package com.mindpalace.app.domain.usecase

import com.mindpalace.app.domain.repository.NoteRepository
import com.mindpalace.app.data.model.Note

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note)
    }
}
