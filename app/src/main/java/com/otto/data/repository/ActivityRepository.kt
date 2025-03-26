package com.otto.data.repository

import com.otto.data.model.ClimbingActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getAllActivities(): Flow<List<ClimbingActivity>>
    suspend fun getActivityById(id: Long): ClimbingActivity?
    suspend fun insertActivity(activity: ClimbingActivity): Long
    suspend fun updateActivity(activity: ClimbingActivity)
    suspend fun deleteActivity(activity: ClimbingActivity)
    suspend fun syncActivities()
} 