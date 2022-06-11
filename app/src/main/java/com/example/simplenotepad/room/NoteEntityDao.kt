package com.example.simplenotepad.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteEntityDao {

    @Query("SELECT * FROM NoteEntity")
    fun getNotes(): Flow<List<NoteEntity>>

    @Insert
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM noteEntity WHERE noteId IN (:listOfNoteEntity)")
    suspend fun deleteNotes(listOfNoteEntity: ArrayList<String>)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)
}