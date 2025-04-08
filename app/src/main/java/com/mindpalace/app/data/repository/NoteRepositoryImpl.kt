package com.mindpalace.app.data.repository

import com.mindpalace.app.data.dao.NoteDao
import com.mindpalace.app.data.model.Note
import com.mindpalace.app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val notesDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = notesDao.getAllNotes()
    override suspend fun insertNote(note: Note) {
        notesDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

}
