package com.example.simplenotepad.room

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithCategories(
    @Embedded val user : NoteEntity,

    @Relation(
        parentColumn = "noteId",
        entityColumn = "noteEntityId"
    )
    val categories: List<CategoryEntity>
)