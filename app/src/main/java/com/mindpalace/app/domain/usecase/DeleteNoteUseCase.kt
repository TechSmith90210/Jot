package com.mindpalace.app.domain.usecase

import com.mindpalace.app.data.model.Note
import com.mindpalace.app.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
