package com.example.simplenotepad.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
@PrimaryKey val noteId: String,
val title : String = "",
val content : String? = "",
val categoryId : String = "",
val color : String = "",
val dateCreated : String = ""
)