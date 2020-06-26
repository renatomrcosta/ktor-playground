package com.xunfos

import com.fasterxml.jackson.databind.SerializationFeature
import com.xunfos.util.log
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
            log("Calling Health Check")
            call.respondText("OK", contentType = ContentType.Text.Plain)
        }
        get("long_wait") {
            doSuspendWork()
        }
        get("long_wait_context") {
            log("Starting Execution of Suspend Work Get")

            withContext(Dispatchers.IO) { doSuspendWork() }

            log("Finishing Execution of Suspend Work Get")
        }
        get("stress_work") {
            log("Starting Execution of Stress Test")

            doStressWork()

            log("Finishing Execution of Stress Test")
        }

        get("stress_work_blocking") {
            log("Starting Execution of Stress Test BLOCKING")

            doStressWorkBlocking()

            log("Finishing Execution of Stress Test BLOCKING ")
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
    log("Before blocking for 10s")
    Thread.sleep(10_000)
    log("Finished blocking for 10s")
}

private suspend fun doSuspendWork() {
    log("Before suspending for 10s")
    delay(10000)
    log("Finished suspending for 10s")
}

private suspend fun doStressWork() {
    coroutineScope {
        log("Before stress work")

        repeat(10_000) {
            launch(Dispatchers.Default) {
                delay(10_000)
                log("Finished Job #$it")
            }
        }
    }
    log("After finishing stress work")
}

private suspend fun doStressWorkBlocking() {
    coroutineScope {
        log("before stress work BLOCKING")

        repeat(10_000) {
            launch(Dispatchers.Default) {
                Thread.sleep(10_000)
                log("Finished Blocking Job #$it")
            }
        }
    }
    log("After finishing stress work BLOCKING")
}
