package com.navi.git

import android.app.Application
import com.navi.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.init(isDebuggable = BuildConfig.DEBUG)
    }
}