package com.otto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "climbing_activities")
data class ClimbingActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val date: Long = System.currentTimeMillis(),
    val latitude: Double,
    val longitude: Double
) 