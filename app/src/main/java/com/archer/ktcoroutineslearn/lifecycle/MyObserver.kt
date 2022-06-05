package com.archer.ktcoroutineslearn.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.archer.ktcoroutineslearn.util.GSLog

class MyObserver : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        GSLog.d(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        GSLog.d(owner)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        GSLog.d(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        GSLog.d(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        GSLog.d(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        GSLog.d(owner)
    }
}