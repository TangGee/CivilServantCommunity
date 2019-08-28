package com.mdove.civilservantcommunity.base.threadpool;

import android.os.Process;
import android.util.Log;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by MDove on 2019/3/25.
 */
public class SSThreadFactory implements ThreadFactory {

    private String mName;
    private static final AtomicInteger sCount = new AtomicInteger(0);

    private SSThreadPriority mPriority = SSThreadPriority.NORMAL;

    public SSThreadFactory(String name) {
        mName = name;
    }

    public SSThreadFactory(String name, SSThreadPriority priority) {
        mName = name;
        mPriority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = mName + "-" + sCount.incrementAndGet();
        Log.i("Thread", "new Thread:" + name);
        return new Thread(r, name) {
            @Override
            public void run() {
                if ( mPriority == SSThreadPriority.LOW ) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                } else if (mPriority == SSThreadPriority.HIGH) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY);
                }
                super.run();
            }
        };
    }

}
