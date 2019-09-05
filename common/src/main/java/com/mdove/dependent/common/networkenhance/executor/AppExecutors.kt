package com.mdove.dependent.common.networkenhance.executor

import java.util.concurrent.Executor

interface AppExecutors {
    val diskIO: Executor
    val networkIO: Executor
    val mainThread: Executor
}