package com.example.projectx

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.projectx.util.PreferenceUtil

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Set the theme as early as possible before any UI is initialized
        val nightMode = PreferenceUtil.loadNightMode(this)

        // Apply night mode globally
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}


