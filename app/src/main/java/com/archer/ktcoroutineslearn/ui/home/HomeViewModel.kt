package com.archer.ktcoroutineslearn.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.archer.ktcoroutineslearn.BaseViewModel
import com.archer.ktcoroutineslearn.util.GSLog
import kotlinx.coroutines.*
import retrofit2.await

class HomeViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun loadData() {
        workerScope?.launch(coroutineExceptionHandler) {
            val bannerDeferred = RetrofitHelper.retrofitService.getHomeList(0)
            val resp = bannerDeferred.await()
            GSLog.json(JSON.toJSONString(resp))
            _text.postValue(resp?.data.toString())
        }
    }
}