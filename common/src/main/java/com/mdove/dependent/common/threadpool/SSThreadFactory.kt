package com.mdove.dependent.common.threadpool

import android.os.Process
import android.util.Log

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by MDove on 2019/3/25.
 */
class SSThreadFactory : ThreadFactory {

    private var mName: String? = null

    private var mPriority = SSThreadPriority.NORMAL

    constructor(name: String) {
        mName = name
    }

    constructor(name: String, priority: SSThreadPriority) {
        mName = name
        mPriority = priority
    }

    override fun newThread(r: Runnable): Thread {
        val name = mName + "-" + sCount.incrementAndGet()
        Log.i("Thread", "new Thread:" + name)
        return object : Thread(r, name) {
            override fun run() {
                if (mPriority == SSThreadPriority.LOW) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                } else if (mPriority == SSThreadPriority.HIGH) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY)
                }
                super.run()
            }
        }
    }

    companion object {
        private val sCount = AtomicInteger(0)
    }

}
