package com.xunfos.ktorPlayground.service

import com.xunfos.ktorPlayground.util.traceLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

private val isolatedScope = CoroutineScope(Executors.newCachedThreadPool().asCoroutineDispatcher())

suspend fun threadBlockingThing() = coroutineScope {
    traceLog("Starting blocking call")
    Thread.sleep(1000)
    traceLog("Finished blocking call")
}

suspend fun isolatedScopeBlockingThing() = withContext(isolatedScope.coroutineContext) {
    traceLog("Starting isolated scope blocking call")
    Thread.sleep(1000)
    traceLog("Finished isolated scope blocking call")
}
