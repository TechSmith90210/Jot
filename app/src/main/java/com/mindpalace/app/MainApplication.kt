package com.mindpalace.app

import android.app.Application
import com.mindpalace.app.core.SupabaseClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SupabaseClient.client
    }
}
