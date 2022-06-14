package com.example.simplenotepad.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["categoryName"], unique = true)])
data class CategoryEntity(
    @PrimaryKey val id : String = "",
    val categoryName : String = ""
){
    companion object{
        const val DEFAULT_CATEGORY_ID = "NONE"

        fun getDefaultCategory(): CategoryEntity {
            return CategoryEntity(DEFAULT_CATEGORY_ID, "NOTES")
        }
    }
}

