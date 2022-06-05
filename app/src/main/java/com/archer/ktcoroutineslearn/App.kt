package com.archer.ktcoroutineslearn

import android.app.Application
import com.archer.ktcoroutineslearn.retrofit.Preference

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Preference.setContext(applicationContext)
    }

}