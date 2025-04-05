package com.example.ottov1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ActivityEntity::class],
    version = 10,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao

    companion object {
        // Migration from 4 to 5: Add grade column
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE activities ADD COLUMN grade TEXT") // Add nullable TEXT column
            }
        }

        // Migration from 5 to 6: Add minPeople and maxPeople columns
        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add columns with NOT NULL and DEFAULT value to handle existing rows
                database.execSQL("ALTER TABLE activities ADD COLUMN minPeople INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE activities ADD COLUMN maxPeople INTEGER NOT NULL DEFAULT 1")
            }
        }

        // Migration from 6 to 7: Add any new columns or changes
        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // If no schema changes, this can be empty
                // Just needed for version bump
            }
        }

        // Migration from 7 to 8: Add any new columns or changes
        val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // If no schema changes, this can be empty
                // Just needed for version bump
            }
        }

        // Migration from 8 to 9: Add any new columns or changes
        val MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // If no schema changes, this can be empty
                // Just needed for version bump
            }
        }
    }
} 