package com.archer.ktcoroutineslearn

import com.archer.ktcoroutineslearn.util.GSLog
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/**
 *
 *
 * ThreadLocal的asContextElement可以在不同协程中设置不同的值；
 * 默认情况下，在一个协程中启动另外一个协程，那么这俩协程就为父子关系，父协程会在子协程全部完成后退出；当父协程终止的时候所有子协程都会终止；
 * 用GlobalScope或者launch(Dispatcher + Job())的方式不会产生父子关系；
 *
 * Dispatchers.Default默认是工作线程，IO是输入输出流线程，Main是UI线程，
 * Unconfined默认会在当前线程但delay后也会变成工作线程，newSingleThreadContext会启动一个新的线程来执行协程；
 *
 *切换协程可以用runBlocking或者withContext函数;
 *
 * launch(Dispatchers.Default + CoroutineName("test")) {
 * 这个+号后面相当于调用了CoroutineContext.plus方法，可以理解为组合了一些CoroutineContext元素，可选的有Job,协程名字等；
 * }
 *
 *
 *
 */
object ContextAndDispatchers {

    suspend fun withContextMethod() = withContext(Dispatchers.Default) {

    }


    private val threadLocal = ThreadLocal<String?>() // declare thread-local variable

    fun threadLocal() = runBlocking<Unit> {
        threadLocal.set("main")
        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(context = Dispatchers.Default + threadLocal.asContextElement("launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }

        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")



    }

    fun contexts() {
        runBlocking {
            launch { // context of the parent, main runBlocking coroutine
                println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
                println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
                println("Default               : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
                println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
               launch(Dispatchers.IO + Job()) { // will get dispatched to DefaultDispatcher
                    println("新的携程               : I'm working in thread ${Thread.currentThread().name}")
                    val jobChild = kotlin.coroutines.coroutineContext[Job]
                    GSLog.d(jobChild)
                }
                val job = kotlin.coroutines.coroutineContext[Job]
                if (job?.children?.iterator()?.hasNext() == true) {
                    GSLog.d(job?.children?.iterator()?.next())
                }
            }

            // launch a coroutine to process some kind of incoming request
            val request = launch(Job(), CoroutineStart.DEFAULT) {
                GSLog.d("I'm working in thread ${Thread.currentThread().name}")
                // it spawns two other jobs
                launch(Job()) {
                    val jobChild = kotlin.coroutines.coroutineContext[Job]
                    GSLog.d("I'm working in thread ${Thread.currentThread().name}")
                    GSLog.d(jobChild)
                    println("job1: I run in my own Job and execute independently!")
                    delay(1000)
                    println("job1: I am not affected by cancellation of the request")
                }
                // and the other inherits the parent context
                launch {
                    val jobChild = kotlin.coroutines.coroutineContext[Job]
                    GSLog.d(jobChild)
                    delay(100)
                    GSLog.d("I'm working in thread ${Thread.currentThread().name}")
                    println("job2: I am a child of the request coroutine")
                    delay(1000)
                    println("job2: I will not execute this line if my parent request is cancelled")
                }
            }
            GSLog.d(request)
            delay(500)
            request.cancel() // cancel processing of the request
            println("main: Who has survived request cancellation?")
            delay(1000) // delay the main thread for a second to see what happens
            val job = kotlin.coroutines.coroutineContext[Job]
            GSLog.d(job?.children?.iterator()?.hasNext())
            job?.children?.forEach {
                GSLog.d(it)
            }

        }

    }
}