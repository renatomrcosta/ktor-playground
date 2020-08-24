package com.xunfos.ktorPlayground.util

fun traceLog(msg: Any) = println("[${getCurrentThreadName()}] $msg")

fun getCurrentThreadName() = Thread.currentThread().name
