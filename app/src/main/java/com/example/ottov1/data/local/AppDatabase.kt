package com.example.ottov1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ActivityEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
} 