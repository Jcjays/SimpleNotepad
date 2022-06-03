package com.example.simplenotepad.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey val id : String = "",
    val categoryName : String = ""
)

