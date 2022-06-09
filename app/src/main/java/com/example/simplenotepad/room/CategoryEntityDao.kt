package com.example.simplenotepad.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryEntityDao {

    @Query("SELECT * FROM CategoryEntity")
    fun getAllCategoryEntities(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

}