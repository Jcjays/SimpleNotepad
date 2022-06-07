package com.example.simplenotepad

interface IClickableState {
    fun onSelectedItem(noteId: String)
    fun onMultipleSelectionEnabled(noteId: String)
}