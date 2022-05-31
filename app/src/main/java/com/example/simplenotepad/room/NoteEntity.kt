package com.example.simplenotepad.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
@PrimaryKey(autoGenerate = true) val noteId: Int,
val title : String = "",
val content : String? = "",
val category : String = "",
val color : String = "DEFAULT",
val dateCreated : String = ""
)