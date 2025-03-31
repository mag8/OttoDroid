package com.example.ottov1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ottov1.data.model.ClimbingActivity
import com.example.ottov1.ui.addedit.ActivityType

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val date: Long,
    val latitude: Double?,
    val longitude: Double?,
    val type: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val location: String?,
    val grade: String?
) {
    fun toClimbingActivity() = ClimbingActivity(
        id = id,
        name = name,
        description = description,
        date = date,
        latitude = latitude,
        longitude = longitude,
        type = ActivityType.valueOf(type),
        startHour = startHour,
        startMinute = startMinute,
        endHour = endHour,
        endMinute = endMinute,
        location = location,
        grade = grade
    )

    companion object {
        fun fromClimbingActivity(activity: ClimbingActivity) = ActivityEntity(
            id = activity.id,
            name = activity.name,
            description = activity.description,
            date = activity.date,
            latitude = activity.latitude,
            longitude = activity.longitude,
            type = activity.type.name,
            startHour = activity.startHour,
            startMinute = activity.startMinute,
            endHour = activity.endHour,
            endMinute = activity.endMinute,
            location = activity.location,
            grade = activity.grade
        )
    }
} 