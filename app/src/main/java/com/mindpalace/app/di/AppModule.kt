package com.mindpalace.app.di

import android.app.Application
import com.mindpalace.app.data.db.NotesDatabase
import com.mindpalace.app.data.repository.NoteRepositoryImpl
import com.mindpalace.app.domain.repository.NoteRepository
import com.mindpalace.app.domain.usecase.DeleteNoteUseCase
import com.mindpalace.app.domain.usecase.GetNotesUseCase
import com.mindpalace.app.domain.usecase.InsertNoteUseCase
import com.mindpalace.app.domain.usecase.NoteUseCases
import com.mindpalace.app.domain.usecase.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDB(app: Application): NotesDatabase {
        return NotesDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideRepo(db: NotesDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao())
    }

    @Provides
    @Singleton
    fun provideNotesUseCases(repository: NoteRepository) : NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            insertNote = InsertNoteUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            updateNote = UpdateNoteUseCase(repository)
        )
    }
}