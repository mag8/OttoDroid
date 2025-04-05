package com.example.ottov1

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OttoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Places SDK initialization will be added later
    }
} 