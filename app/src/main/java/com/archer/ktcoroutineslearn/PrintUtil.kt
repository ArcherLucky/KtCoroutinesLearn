package com.archer.ktcoroutineslearn

import com.archer.ktcoroutineslearn.util.GSLog

fun println(string: Any) {
    GSLog.d(string)
}


fun println(tag: String, string: Any) {
    GSLog.d(tag, string)
}

