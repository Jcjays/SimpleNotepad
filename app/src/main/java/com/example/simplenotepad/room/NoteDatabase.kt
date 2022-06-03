package com.example.simplenotepad.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [NoteEntity::class], version = 2)
abstract class NoteDatabase : RoomDatabase(){

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            if (INSTANCE != null)
                return INSTANCE!!

            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                )
                .fallbackToDestructiveMigration()
                .build()
            return INSTANCE!!
        }
    }

    abstract fun noteDao(): NoteEntityDao
}
