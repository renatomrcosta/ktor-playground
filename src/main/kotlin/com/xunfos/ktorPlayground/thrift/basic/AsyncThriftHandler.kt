package com.xunfos.ktorPlayground.thrift.basic

import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.GetUserResponse
import com.xunfos.playground.thrift.GetUsersResponse
import com.xunfos.playground.thrift.PlaygroundService
import com.xunfos.playground.thrift.User
import org.apache.thrift.async.AsyncMethodCallback
import kotlin.random.Random

class AsyncThriftHandler : PlaygroundService.AsyncIface {
    private val rng = Random(System.currentTimeMillis())

    override fun doWork(resultHandler: AsyncMethodCallback<Void>) {
        val workId = rng.nextLong()

        traceLog("[workId: $workId] Starting doWork Fun. ${WORK_TIME}ms of work")
        Thread.sleep(WORK_TIME)
        traceLog("[workId: $workId] Finishing doWork Fun.")

        resultHandler.onComplete(null)
    }

    override fun ping(resultHandler: AsyncMethodCallback<Void>) {
        traceLog("Pong!")
        resultHandler.onComplete(null)
    }

    override fun getUser(request: GetUserRequest, resultHandler: AsyncMethodCallback<GetUserResponse>) {
        val workId = rng.nextLong()

        traceLog("[workId: $workId] Starting getUser Fun. ${WORK_TIME}ms of work")
        Thread.sleep(WORK_TIME)
        traceLog("[workId: $workId] Finishing getUser Fun.")

        // Suppose a query would be made
        resultHandler.onComplete(
            GetUserResponse().apply {
                this.user = User().apply {
                    this.id = request.id
                    this.name = "banana"
                }
            }
        )
    }

    override fun getUsers(resultHandler: AsyncMethodCallback<GetUsersResponse>) {
        val workId = rng.nextLong()

        traceLog("[workId: $workId] Starting getUsers Fun. ${WORK_TIME}ms of work")
        Thread.sleep(WORK_TIME)
        traceLog("[workId: $workId] Finishing getUsers Fun.")

        resultHandler.onComplete(GetUsersResponse().apply {
            this.users = listOf(
                User().apply {
                    this.id = "1"
                    this.name = "banana"
                })
        })
    }

    companion object {
        private const val WORK_TIME = 5_000L
    }
}
