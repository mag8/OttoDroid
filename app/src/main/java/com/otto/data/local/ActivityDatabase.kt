package com.otto.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.otto.data.model.ClimbingActivity

@Database(
    entities = [ClimbingActivity::class],
    version = 1,
    exportSchema = false
)
abstract class ActivityDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
} 