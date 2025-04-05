package com.example.ottov1.data.repository

import com.example.ottov1.data.local.dao.ActivityDao
import com.example.ottov1.data.local.entity.ActivityEntity
import com.example.ottov1.data.model.ClimbingActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository {
    override fun getAllActivities(): Flow<List<ClimbingActivity>> {
        return activityDao.getAllActivities().map { entities ->
            entities.map { it.toClimbingActivity() }
        }
    }

    override suspend fun getActivityById(id: Long): ClimbingActivity? {
        return activityDao.getActivityById(id)?.toClimbingActivity()
    }

    override suspend fun insertActivity(activity: ClimbingActivity): Long {
        return activityDao.insertActivity(ActivityEntity.fromClimbingActivity(activity))
    }

    override suspend fun updateActivity(activity: ClimbingActivity) {
        activityDao.updateActivity(ActivityEntity.fromClimbingActivity(activity))
    }

    override suspend fun deleteActivity(id: Long) {
        activityDao.deleteActivity(id)
    }
} 