package com.archer.ktcoroutineslearn.collection

import com.alibaba.fastjson.JSON
import com.archer.ktcoroutineslearn.util.GSLog

object CollectionOperator {

    fun operatorDemo() {
        val arrayList = mutableListOf<Int>()
        arrayList.add(0)
        arrayList.add(1)
        arrayList.add(2)
        arrayList.add(3)

        val mapArray = arrayList.map {
            mapOp(it)
        }

        /**
         * associateBy 生成每个value的key，返回一个map一一对应
         */
        val map = arrayList.associateBy {
            it + 1
        }

        GSLog.json(JSON.toJSONString(map))
        GSLog.json(JSON.toJSONString(mapArray))
    }

    private fun mapOp(data: Int): String {
        return "map -> $data"
    }



}