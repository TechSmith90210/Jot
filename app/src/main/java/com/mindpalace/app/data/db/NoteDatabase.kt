package com.mindpalace.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mindpalace.app.data.dao.NoteDao
import com.mindpalace.app.data.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
  abstract fun noteDao(): NoteDao

  companion object {
    @Volatile private var INSTANCE: NotesDatabase? = null

    fun getDatabase(context: Context): NotesDatabase {
      return INSTANCE ?: synchronized(this) {
          val instance = Room.databaseBuilder(context.applicationContext,
              NotesDatabase ::class.java,
              "notes_database"
           ).fallbackToDestructiveMigration().build()
          INSTANCE = instance
          instance
      }
    }
  }
}
