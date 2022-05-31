package com.example.simplenotepad.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId : Int,
    val noteEntityId : Int = 0,
    val categoryName : String = ""
)

