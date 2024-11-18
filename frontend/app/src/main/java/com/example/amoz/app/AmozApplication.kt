package com.example.amoz.app;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AmozApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

