package com.example.simplenotepad.room

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithCategories(
    @Embedded val noteEntity : NoteEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val categoryEntity: CategoryEntity?
)