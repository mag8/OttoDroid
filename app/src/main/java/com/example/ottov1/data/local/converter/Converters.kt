package com.example.ottov1.data.local.converter

import androidx.room.TypeConverter
import com.example.ottov1.ui.addedit.ActivityType

class Converters {
    @TypeConverter
    fun fromActivityType(value: ActivityType): String {
        return value.name
    }

    @TypeConverter
    fun toActivityType(value: String): ActivityType {
        return ActivityType.valueOf(value)
    }
} 