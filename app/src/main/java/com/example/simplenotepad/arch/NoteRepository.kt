package com.example.simplenotepad.arch

import com.example.simplenotepad.room.NoteDatabase
import com.example.simplenotepad.room.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDatabase: NoteDatabase) {

    fun getNotes(): Flow<List<NoteEntity>>{
        return noteDatabase.noteDao().getNotes()
    }

    suspend fun addNote(note: NoteEntity){
        noteDatabase.noteDao().addNote(note)
    }

    suspend fun deleteNote(note: NoteEntity){
        noteDatabase.noteDao().deleteNote(note)
    }

    suspend fun updateNote(note: NoteEntity){
        noteDatabase.noteDao().updateNote(note)
    }

}