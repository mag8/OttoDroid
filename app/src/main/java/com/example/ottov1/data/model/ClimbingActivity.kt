package com.example.ottov1.data.model

import com.example.ottov1.ui.addedit.ActivityType

data class ClimbingActivity(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val date: Long = System.currentTimeMillis(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val type: ActivityType = ActivityType.SPORT,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 10,
    val endMinute: Int = 0,
    val location: String? = null,
    val grade: String? = null
) 