package com.xunfos.ktorPlayground

import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.ktorPlayground.util.withExecutionTime
import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.PlaygroundService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.protocol.TJSONProtocol
import org.apache.thrift.transport.THttpClient
import java.lang.Exception

private const val KTOR_ENDPOINT_URL = "http://localhost:8080/sync/api"
private const val KTOR_COROUTINE_ENDPOINT_URL = "http://localhost:8080/coroutines/api"
private const val SPRING_ENDPOINT_URL = "http://localhost:8080/api"

//How many calls are made to each endpoint
private const val WORK_UNITS = 500

// Use this to test the Thrift-Client
fun main() {
    withExecutionTime {
        try {
            // doAFranklyLudricousAmountOfWork { getBinaryClient(SPRING_ENDPOINT_URL) }
            // doAFranklyLudricousAmountOfWork { getClient(KTOR_ENDPOINT_URL) }
            doAFranklyLudricousAmountOfWork { getClient(KTOR_COROUTINE_ENDPOINT_URL) }
        } catch (e: Exception) {
            traceLog(e.toString())
        }
    }
}

fun doAFranklyLudricousAmountOfWork(clientFactory: () -> PlaygroundService.Client) {
        runBlocking(Dispatchers.IO) {
            //Launches work calls
            launch {
                repeat(WORK_UNITS) {
                    launch {
                        val syncClient = clientFactory()
                        syncClient.doWork()
                    }
                }
            }

            launch {
                //Launches getUser calls
                repeat(WORK_UNITS) { index ->
                    launch {
                        val syncClient = clientFactory()
                        syncClient.getUser(GetUserRequest().apply { id = index.toString() })
                    }
                }
            }

            launch {
                //Launches getUsers calls
                repeat(WORK_UNITS) {
                    launch {
                        val syncClient = clientFactory()
                        syncClient.getUsers()
                    }
                }
            }

            traceLog("Started all jobs")
        }


        traceLog("Ended ludicrous amount of work")


}

private fun getClient(url: String): PlaygroundService.Client {
    // Change this to your local as needed
    val protocolFactory = TJSONProtocol.Factory()
    val transport = THttpClient(url).apply {
        setConnectTimeout(30000)
        setReadTimeout(40000)
    }
    return PlaygroundService.Client(protocolFactory.getProtocol(transport))
}

private fun getBinaryClient(url: String): PlaygroundService.Client {
    // Change this to your local as needed
    val protocolFactory = TBinaryProtocol.Factory()
    val transport = THttpClient(url).apply {
        setConnectTimeout(30000)
        setReadTimeout(40000)
    }
    return PlaygroundService.Client(protocolFactory.getProtocol(transport))
}
