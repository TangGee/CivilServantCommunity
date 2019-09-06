package com.mdove.dependent.common.threadpool

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit

/**
 * Created by MDove on 2019/3/25.
 */
object SSThreadPoolProvider {


    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
    private val KEEP_ALIVE_SECONDS = 10

    val IMMEDIATE_EXECUTOR: ExecutorService = SSThreadPoolExecutor(0, Integer.MAX_VALUE,
            0L, TimeUnit.SECONDS,
            SynchronousQueue(),
            SSThreadFactory("SS-immediate", SSThreadPriority.HIGH))

    val API_EXECUTOR: ExecutorService = SSThreadPoolExecutor(3, 3,
            KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            SSThreadFactory("SS-api"))

    val COMMON_EXECUTOR: ExecutorService = SSThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            SSThreadFactory("SS-common"))

    val BACKGROUND_EXECUTOR = Executors.newSingleThreadExecutor(SSThreadFactory("SS-low", SSThreadPriority.LOW))

}
