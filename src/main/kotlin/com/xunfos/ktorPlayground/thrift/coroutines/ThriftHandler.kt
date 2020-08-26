package com.xunfos.ktorPlayground.thrift.coroutines

import com.xunfos.ktorPlayground.service.isolatedScopeBlockingThing
import com.xunfos.ktorPlayground.service.threadBlockingThing
import com.xunfos.ktorPlayground.util.traceLog
import com.xunfos.playground.thrift.GetUserRequest
import com.xunfos.playground.thrift.GetUserResponse
import com.xunfos.playground.thrift.GetUsersResponse
import com.xunfos.playground.thrift.PlaygroundService
import com.xunfos.playground.thrift.User
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class ThriftHandler : PlaygroundService.Iface {
    private val rng = Random(System.currentTimeMillis())

    override fun doWork() = runBlocking {
        val workId = rng.getId()
        traceLog("[workId: $workId] Starting doWork Fun. ${WORK_TIME}ms of work")

        isolatedScopeBlockingThing()

        traceLog("[workId: $workId] Finishing doWork Fun.")
    }

    override fun ping() {
        traceLog("Pong!")
    }

    override fun getUser(request: GetUserRequest): GetUserResponse = runBlocking {
        val workId = rng.getId()

        traceLog("[workId: $workId] Starting getUser Fun. ${WORK_TIME}ms of work")

        threadBlockingThing()

        traceLog("[workId: $workId] Finishing getUser Fun.")

        // Suppose a query would be made
        GetUserResponse().apply {
            this.user = User().apply {
                this.id = request.id
                this.name = "banana"
            }
        }
    }

    override fun getUsers(): GetUsersResponse = runBlocking {
        val workId = rng.getId()

        traceLog("[workId: $workId] Starting getUsers Fun. ${WORK_TIME}ms of work")

        threadBlockingThing()

        traceLog("[workId: $workId] Finishing getUsers Fun.")

        GetUsersResponse().apply {
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

private fun Random.getId(): Int = this.nextInt(1, 10_000)
