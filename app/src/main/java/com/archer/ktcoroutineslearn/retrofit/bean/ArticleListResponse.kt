package com.archer.ktcoroutineslearn.retrofit.bean

data class ArticleListResponse(
    var errorCode: Int,
    var errorMsg: String?,
    var data: Data
)