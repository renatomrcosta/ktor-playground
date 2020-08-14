package com.xunfos.ktorPlayground.thrift

import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.GetUserResponse
import com.xunfos.playground.thrift.GetUsersResponse
import com.xunfos.playground.thrift.PlaygroundService
import org.apache.thrift.async.AsyncMethodCallback

class AsyncThriftHandler : PlaygroundService.AsyncIface {
    override fun doWork(resultHandler: AsyncMethodCallback<Void>?) {
        traceLog("hello there, general kenobi")
        TODO("Not yet implemented")
    }

    override fun ping(resultHandler: AsyncMethodCallback<Void>?) {
        traceLog("hello there, general kenobi")
        TODO("Not yet implemented")
    }

    override fun getUser(request: GetUserRequest?, resultHandler: AsyncMethodCallback<GetUserResponse>?) {
        traceLog("hello there, general kenobi")
        TODO("Not yet implemented")
    }

    override fun getUsers(resultHandler: AsyncMethodCallback<GetUsersResponse>?) {
        traceLog("hello there, general kenobi")
        TODO("Not yet implemented")
    }
}
