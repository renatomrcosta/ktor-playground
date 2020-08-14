package com.xunfos.ktorPlayground

import com.fasterxml.jackson.databind.SerializationFeature
import com.xunfos.ktorPlayground.thrift.AsyncThriftHandler
import com.xunfos.ktorPlayground.thrift.ThriftHandler
import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.playground.thrift.PlaygroundService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.thrift.protocol.TJSONProtocol
import org.apache.thrift.transport.TMemoryBuffer
import org.apache.thrift.transport.TMemoryInputTransport

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
        post("/api") {
            traceLog("started api call")
            val processor = PlaygroundService.Processor<PlaygroundService.Iface>(ThriftHandler())
            val request = call.receive<ByteArray>()

            val inputProtocol = TJSONProtocol(TMemoryInputTransport(request))
            val outputBuffer = TMemoryBuffer(0)
            val outputProtocol = TJSONProtocol(outputBuffer)

            try {
                processor.process(inputProtocol, outputProtocol)
                // val response = outputProtocol.readString()
                val response = outputBuffer.toString(Charsets.UTF_8)
                call.respondText(response)
            } catch (e: Exception) {
                traceLog("Exception")
                traceLog(e)
            }
        }

        post("/async") {
            val processor = PlaygroundService.AsyncProcessor<PlaygroundService.AsyncIface>(AsyncThriftHandler())
            val request = call.receive<ByteArray>()

            val inputProtocol = TJSONProtocol(TMemoryInputTransport(request))
            val outputBuffer = TMemoryBuffer(0)
            val outputProtocol = TJSONProtocol(outputBuffer)

            try {
                processor.process(inputProtocol, outputProtocol)
                // val response = outputProtocol.readString()
                val response = outputBuffer.toString(Charsets.UTF_8)
                call.respondText(response)
            } catch (e: Exception) {
                traceLog("Exception")
                traceLog(e)
            }
        }

        get("/healthcheck") {
            traceLog("Calling Health Check")
            call.respondText("OK", contentType = ContentType.Text.Plain)
        }
        get("long_wait") {
            doSuspendWork()
        }
        get("long_wait_context") {
            traceLog("Starting Execution of Suspend Work Get")

            withContext(Dispatchers.IO) { doSuspendWork() }

            traceLog("Finishing Execution of Suspend Work Get")
        }
        get("stress_work") {
            traceLog("Starting Execution of Stress Test")

            doStressWork()

            traceLog("Finishing Execution of Stress Test")
        }

        get("stress_work_blocking") {
            traceLog("Starting Execution of Stress Test BLOCKING")

            doStressWorkBlocking()

            traceLog("Finishing Execution of Stress Test BLOCKING ")
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
    traceLog("Before blocking for 10s")
    Thread.sleep(10_000)
    traceLog("Finished blocking for 10s")
}

private suspend fun doSuspendWork() {
    traceLog("Before suspending for 10s")
    delay(10000)
    traceLog("Finished suspending for 10s")
}

private suspend fun doStressWork() {
    coroutineScope {
        traceLog("Before stress work")

        repeat(10_000) {
            launch(Dispatchers.Default) {
                delay(10_000)
                traceLog("Finished Job #$it")
            }
        }
    }
    traceLog("After finishing stress work")
}

private suspend fun doStressWorkBlocking() {
    coroutineScope {
        traceLog("before stress work BLOCKING")

        repeat(10_000) {
            launch(Dispatchers.Default) {
                Thread.sleep(10_000)
                traceLog("Finished Blocking Job #$it")
            }
        }
    }
    traceLog("After finishing stress work BLOCKING")
}
