package com.archer.ktcoroutineslearn

import androidx.lifecycle.ViewModel
import com.archer.ktcoroutineslearn.util.GSLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

open class BaseViewModel: ViewModel() {

    protected var workerScope: CoroutineScope? = null

    protected val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
        GSLog.e(throwable.toString())
    }

    init {
        workerScope = CoroutineScope(Dispatchers.IO)
    }

    fun cancelJobs() {
        workerScope?.cancel()
        workerScope = null
    }
}