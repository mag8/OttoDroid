package com.otto.data.repository

import android.location.LocationManager
import com.otto.data.local.ActivityDao
import com.otto.data.model.ClimbingActivity
import com.otto.data.remote.ActivityRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao,
    private val remoteDataSource: ActivityRemoteDataSource,
    private val locationManager: LocationManager
) : ActivityRepository {

    override fun getAllActivities(): Flow<List<ClimbingActivity>> {
        return activityDao.getAllActivities()
    }

    override suspend fun getActivityById(id: Long): ClimbingActivity? {
        return activityDao.getActivityById(id)
    }

    override suspend fun insertActivity(activity: ClimbingActivity): Long {
        return activityDao.insertActivity(activity)
    }

    override suspend fun updateActivity(activity: ClimbingActivity) {
        activityDao.updateActivity(activity)
    }

    override suspend fun deleteActivity(activity: ClimbingActivity) {
        activityDao.deleteActivity(activity)
    }

    override suspend fun syncActivities() {
        val remoteActivities = remoteDataSource.fetchActivities()
        remoteActivities.forEach { activity ->
            activityDao.insertActivity(activity)
        }
    }
} 