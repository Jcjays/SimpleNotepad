package com.example.simplenotepad.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotepad.room.CategoryEntity
import com.example.simplenotepad.room.NoteDatabase
import com.example.simplenotepad.room.NoteEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {

    private lateinit var repository: NoteRepository

    val noteEntitiesLiveData = MutableLiveData<List<NoteEntity>>()
    val categoryEntitiesLiveData = MutableLiveData<List<CategoryEntity>>()

    val transactionCompleteListener = MutableLiveData<Event<Boolean>>()
    var onSelectionModeEnable = MutableLiveData<Boolean>()

    fun init(noteDatabase: NoteDatabase){
        repository = NoteRepository(noteDatabase)

        viewModelScope.launch {
            repository.getNotes().collect { items ->
                noteEntitiesLiveData.postValue(items)
            }
        }

        viewModelScope.launch {
            repository.getAllCategories().collect { items ->
                categoryEntitiesLiveData.postValue(items)
            }
        }

    }


    data class CategoriesViewState(
        val isLoading : Boolean = false,
        val categoryEntities: List<CategoryItem> = emptyList(),
        ){
            data class CategoryItem(
            val category: CategoryEntity = CategoryEntity()
            )
    }

    //region note entity
    fun addNote(note: NoteEntity) = viewModelScope.launch {
        repository.addNote(note)
        transactionCompleteListener.postValue(Event(true))
    }

    fun deleteNotes(listOfNoteEntity: ArrayList<String>) = viewModelScope.launch {
        repository.deleteNotes(listOfNoteEntity)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch {
        repository.updateNote(note)
    }
    //endregion note entity

    //region category entity

    fun addCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.addCategory(category)
        transactionCompleteListener.postValue(Event(true))
    }

    fun deleteCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.deleteCategory(category)
    }

    fun updateCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.updateCategory(category)
    }

    //endregion category entity
}