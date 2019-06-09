package com.harsh.typicode.utils

import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class NetworkScheduler : Scheduler {
    override fun asRxScheduler(): io.reactivex.Scheduler = Schedulers.from(executor)

    private val executor = Executors.newFixedThreadPool(2)

    override fun runOnThread(runnable: ExecutionBlock) {
        executor.execute(runnable)
    }
}