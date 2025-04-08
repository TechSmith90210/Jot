package com.mindpalace.app.domain.usecase

import javax.inject.Inject

class NoteUseCases @Inject constructor (
    val getNotes: GetNotesUseCase,
    val insertNote: InsertNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val updateNote: UpdateNoteUseCase,
)
