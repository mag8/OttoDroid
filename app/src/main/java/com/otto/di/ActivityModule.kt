package com.otto.di

import android.content.Context
import android.location.LocationManager
import androidx.room.Room
import com.otto.data.local.ActivityDatabase
import com.otto.data.local.ActivityDao
import com.otto.data.remote.ActivityRemoteDataSource
import com.otto.data.repository.ActivityRepository
import com.otto.data.repository.ActivityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityModule {
    
    @Provides
    @Singleton
    fun provideActivityDatabase(@ApplicationContext context: Context): ActivityDatabase {
        return Room.databaseBuilder(
            context,
            ActivityDatabase::class.java,
            "activities.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideActivityDao(database: ActivityDatabase): ActivityDao {
        return database.activityDao()
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    
    @Provides
    @Singleton
    fun provideActivityRepository(
        activityDao: ActivityDao,
        remoteDataSource: ActivityRemoteDataSource,
        locationManager: LocationManager
    ): ActivityRepository {
        return ActivityRepositoryImpl(activityDao, remoteDataSource, locationManager)
    }
    
    // Other providers...
} 