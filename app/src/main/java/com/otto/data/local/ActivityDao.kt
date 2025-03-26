package com.otto.data.local

import androidx.room.*
import com.otto.data.model.ClimbingActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM climbing_activities ORDER BY date DESC")
    fun getAllActivities(): Flow<List<ClimbingActivity>>

    @Query("SELECT * FROM climbing_activities WHERE id = :id")
    suspend fun getActivityById(id: Long): ClimbingActivity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ClimbingActivity): Long

    @Update
    suspend fun updateActivity(activity: ClimbingActivity)

    @Delete
    suspend fun deleteActivity(activity: ClimbingActivity)
} 