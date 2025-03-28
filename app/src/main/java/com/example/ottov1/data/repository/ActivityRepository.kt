package com.example.ottov1.data.repository

import com.example.ottov1.data.model.ClimbingActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getAllActivities(): Flow<List<ClimbingActivity>>
    suspend fun getActivityById(id: Long): ClimbingActivity?
    suspend fun insertActivity(activity: ClimbingActivity): Long
    suspend fun updateActivity(activity: ClimbingActivity)
    suspend fun deleteActivity(id: Long)
} 