package com.example.kotlinnotesapp.domain.usecase

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val insertNote: InsertNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val updateNote: UpdateNoteUseCase,
)
