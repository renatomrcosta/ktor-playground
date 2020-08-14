package com.xunfos.ktorPlayground.thrift

import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.GetUserResponse
import com.xunfos.playground.thrift.GetUsersResponse
import com.xunfos.playground.thrift.PlaygroundService

class ThriftHandler : PlaygroundService.Iface {
    override fun doWork() {
        TODO("Not yet implemented")
    }

    override fun ping() {
        TODO("Not yet implemented")
    }

    override fun getUser(request: GetUserRequest): GetUserResponse {
        TODO("Not yet implemented")
    }

    override fun getUsers(): GetUsersResponse {
        TODO("Not yet implemented")
    }
}
