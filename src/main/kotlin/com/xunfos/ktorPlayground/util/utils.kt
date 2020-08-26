package com.xunfos.ktorPlayground.util

import kotlin.system.measureTimeMillis

fun traceLog(msg: Any) = println("[${getCurrentThreadName()}] $msg")

fun getCurrentThreadName() = Thread.currentThread().name

inline fun withExecutionTime(block: () -> Unit) = measureTimeMillis {
    block()
}.run { println("Total Execution time: ${this}ms") }
