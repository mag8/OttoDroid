package com.example.ottov1.data.model

data class ClimbingActivity(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val date: Long = System.currentTimeMillis(),
    val latitude: Double? = null,
    val longitude: Double? = null
) 