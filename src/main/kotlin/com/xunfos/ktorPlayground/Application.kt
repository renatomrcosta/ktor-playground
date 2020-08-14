package com.xunfos.ktorPlayground

import com.fasterxml.jackson.databind.SerializationFeature
import com.xunfos.ktorPlayground.util.trace
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/healthcheck") {
            trace("Calling Health Check")
            call.respondText("OK", contentType = ContentType.Text.Plain)
        }
        get("long_wait") {
            doSuspendWork()
        }
        get("long_wait_context") {
            trace("Starting Execution of Suspend Work Get")

            withContext(Dispatchers.IO) { doSuspendWork() }

            trace("Finishing Execution of Suspend Work Get")
        }
        get("stress_work") {
            trace("Starting Execution of Stress Test")

            doStressWork()

            trace("Finishing Execution of Stress Test")
        }

        get("stress_work_blocking") {
            trace("Starting Execution of Stress Test BLOCKING")

            doStressWorkBlocking()

            trace("Finishing Execution of Stress Test BLOCKING ")
        }

        get("long_wait_run_blocking") {
            runBlocking {
                doSuspendWork()
            }
        }
        get("long_wait_blocking") {
            doBlockingWork()
        }
    }
}

private fun doBlockingWork() {
    trace("Before blocking for 10s")
    Thread.sleep(10_000)
    trace("Finished blocking for 10s")
}

private suspend fun doSuspendWork() {
    trace("Before suspending for 10s")
    delay(10000)
    trace("Finished suspending for 10s")
}

private suspend fun doStressWork() {
    coroutineScope {
        trace("Before stress work")

        repeat(10_000) {
            launch(Dispatchers.Default) {
                delay(10_000)
                trace("Finished Job #$it")
            }
        }
    }
    trace("After finishing stress work")
}

private suspend fun doStressWorkBlocking() {
    coroutineScope {
        trace("before stress work BLOCKING")

        repeat(10_000) {
            launch(Dispatchers.Default) {
                Thread.sleep(10_000)
                trace("Finished Blocking Job #$it")
            }
        }
    }
    trace("After finishing stress work BLOCKING")
}
