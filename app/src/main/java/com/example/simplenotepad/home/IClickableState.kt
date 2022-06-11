package com.example.simplenotepad.home

interface IClickableState {
    fun onSelectedItem(noteId: String)
    fun isEmpty(isSelected: Boolean)
    fun onMultipleSelectionEnabled(noteId: String)
}