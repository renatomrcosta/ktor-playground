package com.xunfos.ktorPlayground.thrift

import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.GetUserResponse
import com.xunfos.playground.thrift.GetUsersResponse
import com.xunfos.playground.thrift.PlaygroundService
import com.xunfos.playground.thrift.User
import kotlin.random.Random

class ThriftHandler : PlaygroundService.Iface {
    private val rng = Random(System.currentTimeMillis())

    override fun doWork() {
        val workId = rng.nextLong()

        traceLog("[workId: $workId] Starting doWork Fun. ${WORK_TIME}ms of work")
        Thread.sleep(WORK_TIME)
        traceLog("[workId: $workId] Finishing doWork Fun.")
    }

    override fun ping() {
        traceLog("Pong!")
    }

    override fun getUser(request: GetUserRequest): GetUserResponse {
        val workId = rng.nextLong()

        traceLog("[workId: $workId] Starting getUser Fun. ${WORK_TIME}ms of work")
        Thread.sleep(WORK_TIME)
        traceLog("[workId: $workId] Finishing getUser Fun.")

        // Suppose a query would be made
        return GetUserResponse().apply {
            this.user = User().apply {
                this.id = request.id
                this.name = "banana"
            }
        }
    }

    override fun getUsers(): GetUsersResponse {
        val workId = rng.nextLong()

        traceLog("[workId: $workId] Starting getUsers Fun. ${WORK_TIME}ms of work")
        Thread.sleep(WORK_TIME)
        traceLog("[workId: $workId] Finishing getUsers Fun.")

        return GetUsersResponse().apply {
            this.users = listOf(
                User().apply {
                    this.id = "1"
                    this.name = "banana"
                })
        }
    }

    companion object {
        private const val WORK_TIME = 5_000L
    }
}
