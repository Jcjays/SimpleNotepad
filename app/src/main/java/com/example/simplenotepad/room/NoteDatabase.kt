package com.example.simplenotepad.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [NoteEntity::class, CategoryEntity::class], version = 4, exportSchema = false)
abstract class NoteDatabase : RoomDatabase(){

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            if (INSTANCE != null)
                return INSTANCE!!

            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                )
                .addMigrations(MIGRATION_2_3(), MIGRATION_3_4())
                .build()
            return INSTANCE!!
        }

        class MIGRATION_2_3 : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `CategoryEntity` (`id` TEXT NOT NULL, `categoryName` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        class MIGRATION_3_4 : Migration(3,4){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_CategoryEntity_categoryName` ON `CategoryEntity` (categoryName)")
            }
        }

    }

    abstract fun noteDao(): NoteEntityDao
    abstract fun categoryDao(): CategoryEntityDao
}
