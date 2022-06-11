package com.example.simplenotepad.arch

import com.example.simplenotepad.room.CategoryEntity
import com.example.simplenotepad.room.NoteDatabase
import com.example.simplenotepad.room.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDatabase: NoteDatabase) {


    //region note entity
    fun getNotes(): Flow<List<NoteEntity>>{
        return noteDatabase.noteDao().getNotes()
    }

    suspend fun addNote(note: NoteEntity){
        noteDatabase.noteDao().addNote(note)
    }

    suspend fun deleteNotes(listOfNoteEntity: ArrayList<String>){
        noteDatabase.noteDao().deleteNotes(listOfNoteEntity)
    }

    suspend fun deleteNote(note: NoteEntity){
        noteDatabase.noteDao().deleteNote(note)
    }

    suspend fun updateNote(note: NoteEntity){
        noteDatabase.noteDao().updateNote(note)
    }
    //endregion note entity

    //region category entity
    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return noteDatabase.categoryDao().getAllCategoryEntities()
    }

    suspend fun addCategory(categoryEntity: CategoryEntity) {
        noteDatabase.categoryDao().insert(categoryEntity)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        noteDatabase.categoryDao().delete(categoryEntity)
    }

    suspend fun updateCategory(categoryEntity: CategoryEntity) {
        noteDatabase.categoryDao().update(categoryEntity)
    }
    //endregion category entity
}