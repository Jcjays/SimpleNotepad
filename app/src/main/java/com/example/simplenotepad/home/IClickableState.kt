package com.example.simplenotepad.home

interface IClickableState {
    fun onSelectedItem(noteId: String)
    fun onMultipleSelectionEnabled(noteId: String)
}