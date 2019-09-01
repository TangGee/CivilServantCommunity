package com.mdove.civilservantcommunity.base.threadpool

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by MDove on 2019/3/25.
 */
class SSThreadPoolExecutor : ThreadPoolExecutor {
    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit, workQueue: BlockingQueue<Runnable>) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue) {}

    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit, workQueue: BlockingQueue<Runnable>, threadFactory: ThreadFactory) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory) {}

    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit, workQueue: BlockingQueue<Runnable>, handler: RejectedExecutionHandler) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler) {}

    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit, workQueue: BlockingQueue<Runnable>, threadFactory: ThreadFactory, handler: RejectedExecutionHandler) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler) {}

    override fun afterExecute(r: Runnable, t: Throwable?) {
        super.afterExecute(r, t)
        var runnableException: Throwable? = null
        if (t == null && r is Future<*>) {
            val future = r as Future<*>
            if (future.isDone && !future.isCancelled) {
                try {
                    future.get()
                } catch (e: InterruptedException) {
                    // ignore
                } catch (e: ExecutionException) {
                    runnableException = e.cause
                } catch (e: Throwable) {
                    // ignore
                }

            }
        }
        if (runnableException != null) {
            throw RuntimeException(runnableException)
        }
    }
}
