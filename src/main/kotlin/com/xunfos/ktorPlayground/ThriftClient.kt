package com.xunfos.ktorPlayground

import com.xunfos.playground.thrift.PlaygroundService
import org.apache.thrift.async.TAsyncClientManager
import org.apache.thrift.protocol.TJSONProtocol
import org.apache.thrift.transport.THttpClient
import org.apache.thrift.transport.TNonblockingSocket

// Use this to test the Thrift-Client
fun main() {
    val syncClient = getClient("http://localhost:8080/sync/api")
    val asyncClient = getAsyncClient()
    syncClient.ping()
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

private fun getAsyncClient(): PlaygroundService.AsyncClient {
    // Change this to your local as needed
    val protocolFactory = TJSONProtocol.Factory()
    val clientManager = TAsyncClientManager()
    val transport = TNonblockingSocket("localhost", 8081)
    return PlaygroundService.AsyncClient(protocolFactory, clientManager, transport)
}
