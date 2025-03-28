package com.example.ottov1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ottov1.data.model.ClimbingActivity

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val date: Long,
    val latitude: Double?,
    val longitude: Double?
) {
    fun toClimbingActivity() = ClimbingActivity(
        id = id,
        name = name,
        description = description,
        date = date,
        latitude = latitude,
        longitude = longitude
    )

    companion object {
        fun fromClimbingActivity(activity: ClimbingActivity) = ActivityEntity(
            id = activity.id,
            name = activity.name,
            description = activity.description,
            date = activity.date,
            latitude = activity.latitude,
            longitude = activity.longitude
        )
    }
} 