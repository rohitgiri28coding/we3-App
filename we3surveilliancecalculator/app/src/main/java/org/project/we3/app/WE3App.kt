package org.project.we3.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WE3App: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}