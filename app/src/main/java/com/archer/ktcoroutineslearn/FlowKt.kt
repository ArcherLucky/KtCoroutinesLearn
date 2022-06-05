package com.archer.ktcoroutineslearn

import com.archer.ktcoroutineslearn.util.GSLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

object FlowKt {

    /**
     * Flow类型的构建器函数称为flow。
     * 构建器块内的代码flow { ... }可以挂起（suspend）。
     * 该simple函数不再用suspend修饰符标记。
     * 使用emit函数从流中发出值。
     * 使用collect函数从流中收集值。
     */
    private fun simpleFlow(): Flow<Int> = flow { // flow builder
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            emit(i) // emit next value
        }
    }

    /**
     * Notice how flow { ... } works in the background thread, while collection happens in the main thread:
     *
     * Another thing to observe here is that the flowOn operator has changed the default sequential nature of the flow.
     * Now collection happens in one coroutine ("coroutine#1") and emission happens in another coroutine ("coroutine#2")
     * that is running in another thread concurrently with the collecting coroutine.
     * The flowOn operator creates another coroutine for an upstream flow when it has to change the CoroutineDispatcher in its context.
     *
     * flowOn(Dispatchers.Default)改变了"上游(数据生成)"的协程上下文，所以Thread.sleep是运行在工作线程的，而调用它的
     * @see com.archer.ktcoroutineslearn.FlowKt.flowOnTest 运行在main线程，这个操作符改变了FLOW上下游顺序执行的特性，使得"上游(数据生成)"运行在工作线程，
     * collect 下游协程运行在main线程
     */
    private fun simpleFlowOn(): Flow<Int> = flow {
        GSLog.d()
        for (i in 1..3) {
            GSLog.d(Thread.currentThread().name)
            Thread.sleep(1000L)
            emit(i)
        }
    }.flowOn(Dispatchers.Default)


    fun flowOnTest() = runBlocking {
        simpleFlowOn().collect {
            GSLog.d("FlowKt", Thread.currentThread().name)
        }
    }

    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/

    private fun slowFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100) // pretend we are asynchronously waiting 100 ms
            emit(i) // emit next value
        }
    }

    /**
     * Flow buffer操作符
     */
    fun bufferFlowTest() = runBlocking<Unit> {
        val time = measureTimeMillis {
            slowFlow().buffer().collect { value ->
                delay(300) // pretend we are processing it for 300 ms
                println(value)
            }
        }
        println("Collected in $time ms")
    }

    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/


    /**
     * When a flow represents partial results of the operation or operation status updates, it may not be necessary to process each value, but instead, only most recent ones.
     * In this case, the conflate operator can be used to skip intermediate values when a collector is too slow to process them.
     *
     * conflate自动忽略暂时没时间处理的值，只处理"最近的"值，下面这个例子，slowFlow每100ms发送一个数据，
     * 但collect中delay了300ms，所以"2"这个值发送的时候还在delay，就不会得到处理
     *
     */
    fun conflateFlowTest() = runBlocking {
        val time = measureTimeMillis {
            slowFlow()
                .conflate() // conflate emissions, don't process each one
                .collect { value ->
                    delay(300) // pretend we are processing it for 300 ms
                    println(value)
                }
        }
        println("conflate Collected in $time ms")
    }


    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/


    /**
     * collectLatest操作符的意思是处理最后的一个值，
     */
    fun collectLatestTest() = runBlocking {
        val time = measureTimeMillis {
            slowFlow()
                .collectLatest { value -> // cancel & restart on the latest value
                    println("Collecting $value")
                    delay(120) // pretend we are processing it for 300 ms
                    println("Done $value")
                }
        }
        println("Collected in $time ms")
    }


    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/


    /**
     * 简单demo一下Flow
     */
    fun flowTest() = runBlocking<Unit> {
        // Launch a concurrent coroutine to check if the main thread is blocked
        launch {
            for (k in 1..3) {
                println(FlowKt::class.java.simpleName, "I'm not blocked $k")
                delay(100)
            }
        }
        // Collect the flow
        simpleFlow().collect { value -> println(value) }
    }

    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/


    /**
     * Flows are cold streams similar to sequences — the code inside a flow builder does not run until the flow is collected.
     * 流是类似于序列的冷流——流构建器中的代码在收集流之前不会运行。
     *
     * 意思就是flow{}中的代码，尽管已经构造出来，但是只要不调用collect就不会运行；
     *
     * 这是simple函数（返回流）没有用suspend修饰符标记的关键原因。
     * 就其本身而言，simple()调用会快速返回，并且不会等待任何内容。
     * 每次收集流都会启动，这就是我们collect再次调用时看到“流启动”的原因。
     */

    fun showFlowIsCold() = runBlocking<Unit> {
        println("Calling simple function...")
        val flow = simpleFlow()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/


    fun cancelFlow() = runBlocking<Unit> {

        /**
         * flowOf，把一个数据包装成flow
         */
        flowOf("这是用flowOf构建的数组Flow1", "这是用flowOf构建的数组Flow2").collect {
            println(it)
        }

        /**
         * 把一个数据as Flow一下
         */
        (1..3).asFlow().collect {
            println(it)
        }

        withTimeoutOrNull(250) { // Timeout after 250ms
            simpleFlow().collect { value -> println(value) }
        }
        println("Done")
    }

    /** ---------------------------------------------------分割线--------------------------------------------------------------------- **/


    suspend fun flowMapOperator() {
        (1..3).asFlow().map {
            performRequest(it)
        }.collect { resp ->
            println("使用flow操作符int转成string -> $resp")
        }
    }

    private suspend fun performRequest(request: Int): String {
        delay(1000) // imitate long-running asynchronous work
        return "response $request"
    }

    private suspend fun terminalFlowOperator() {
        val sum = (1..5).asFlow()
            .map { it * it } // squares of numbers from 1 to 5
            .reduce{ a, b -> a * b }
        println(sum)
    }

}