package com.otto.data.remote

import com.otto.data.model.ClimbingActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRemoteDataSource @Inject constructor() {
    // TODO: Implement remote data source operations
    suspend fun fetchActivities(): List<ClimbingActivity> {
        // Implement actual remote data fetching
        return emptyList()
    }

    suspend fun uploadActivity(activity: ClimbingActivity): Result<ClimbingActivity> {
        // Implement actual remote data upload
        return Result.success(activity)
    }
} 