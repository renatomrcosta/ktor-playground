package com.xunfos.ktorPlayground.thrift

import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.GetUserResponse
import com.xunfos.playground.thrift.GetUsersResponse
import com.xunfos.playground.thrift.PlaygroundService

class ThriftHandler : PlaygroundService.Iface {
    override fun doWork() {
        traceLog("hello there!")
        TODO("Not yet implemented")
    }

    override fun ping() {
        traceLog("hello there!")
        TODO("Not yet implemented")
    }

    override fun getUser(request: GetUserRequest): GetUserResponse {
        traceLog("hello there!")
        TODO("Not yet implemented")
    }

    override fun getUsers(): GetUsersResponse {
        traceLog("hello there!")
        TODO("Not yet implemented")
    }
}
