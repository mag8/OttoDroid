package com.example.ottov1.di

import com.example.ottov1.util.ContextStringProvider
import com.example.ottov1.util.StringProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindStringProvider(impl: ContextStringProvider): StringProvider

    // Add other application-level bindings here if needed
} 