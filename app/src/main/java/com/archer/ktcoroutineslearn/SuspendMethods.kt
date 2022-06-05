package com.archer.ktcoroutineslearn

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * 挂起函数的组合使用，挂起函数中使用delay来代替thread.sleep
 */
object SuspendMethods {

    /**
     * 最常见的suspend函数(挂起函数)，只能在协程中使用，以同步的编码风格写异步
     */
    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // pretend we are doing something useful here
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // pretend we are doing something useful here, too
        return 29
    }


    fun main() {
        /**
         * runBlocking相当于启动了一个协程
         */
        runBlocking {
            /**
             * 最简单的执行方式，顺序执行
             */
            val time = measureTimeMillis {
                val one = doSomethingUsefulOne()
                val two = doSomethingUsefulTwo()
                println("The answer is ${one + two}")
            }
            println("Completed in $time ms")

            /**
             * 异步并发执行，async和await成对出现，调用await相当于执行了async包裹的代码
             *
             * 从概念上讲，async就像launch。它启动一个单独的协程，它是一个轻量级线程，可与所有其他协程同时工作
             * 。不同之处在于它launch返回一个Job并且不携带任何结果值，而async返回一个Deferred - 一个轻量级的非阻塞未来，表示稍后提供结果的承诺。
             * 您可以使用.await()on 延迟值来获得其最终结果，但Deferred它也是 a Job，因此您可以在需要时取消它。
             */
            val asyncTime = measureTimeMillis {
                val one = async { doSomethingUsefulOne() }
                val two = async { doSomethingUsefulTwo() }
                println("The answer is ${one.await() + two.await()}")
                one.cancel()
            }
            println("Completed in $time ms")

            /**
             * 需要调用start才可用，相当于懒惰式加载
             */
            val lazyAsyncTime = measureTimeMillis {
                val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
                val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
                // some computation
                one.start() // start the first one
                two.start() // start the second one
                println("The answer is ${one.await() + two.await()}")
            }
            println("Completed in $time ms")

        }
    }

}