package com.xunfos.util

fun log(msg: String) = println("[${getCurrentThreadName()}] $msg")
fun getCurrentThreadName() = Thread.currentThread().name
