package com.otto

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration

@HiltAndroidApp
class OttoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize OSMDroid
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
        
        // Set user agent to prevent getting banned from OSM servers
        Configuration.getInstance().userAgentValue = "com.otto"
    }
} 