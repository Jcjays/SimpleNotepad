package com.example.simplenotepad.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotepad.room.NoteDatabase
import com.example.simplenotepad.room.NoteEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.w3c.dom.Entity

class NoteViewModel : ViewModel() {

    private lateinit var repository: NoteRepository

    val noteEntities = MutableLiveData<List<NoteEntity>>()

    val transactionCompleteListener = MutableLiveData<Event<Boolean>>()

    fun init(noteDatabase: NoteDatabase){
        repository = NoteRepository(noteDatabase)

        viewModelScope.launch {
            repository.getNotes().collect { items ->
                noteEntities.postValue(items)
            }
        }
    }


    fun addNote(note: NoteEntity) = viewModelScope.launch {
        repository.addNote(note)
        transactionCompleteListener.postValue(Event(true))
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch {
        repository.updateNote(note)
    }

}