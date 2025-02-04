package com.example.kotlinnotesapp.data.repository

import com.example.kotlinnotesapp.data.dao.NoteDao
import com.example.kotlinnotesapp.data.model.Note
import com.example.kotlinnotesapp.domain.repository.NoteRepository
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
