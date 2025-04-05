package com.example.ottov1.di

import android.content.Context
import androidx.room.Room
import com.example.ottov1.data.local.ActivityDao
import com.example.ottov1.data.local.AppDatabase
import com.example.ottov1.data.repository.ActivityRepository
import com.example.ottov1.data.repository.ActivityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "otto_database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideActivityDao(database: AppDatabase): ActivityDao {
        return database.activityDao()
    }

    @Provides
    @Singleton
    fun provideActivityRepository(activityDao: ActivityDao): ActivityRepository {
        return ActivityRepositoryImpl(activityDao)
    }
} 