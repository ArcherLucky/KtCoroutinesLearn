package com.archer.ktcoroutineslearn.retrofit.bean

import com.archer.ktcoroutineslearn.retrofit.bean.Data

data class HomeListResponse(
    var errorCode: Int,
    var errorMsg: String?,
    var data: Data
)