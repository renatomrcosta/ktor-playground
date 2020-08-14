package com.xunfos.ktorPlayground.util

fun trace(msg: String) = println("[${getCurrentThreadName()}] $msg")

fun getCurrentThreadName() = Thread.currentThread().name
