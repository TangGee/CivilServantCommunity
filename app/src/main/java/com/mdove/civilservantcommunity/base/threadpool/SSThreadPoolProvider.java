package com.mdove.civilservantcommunity.base.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by MDove on 2019/3/25.
 */
public class SSThreadPoolProvider {




    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 10;

    public static final ExecutorService IMMEDIATE_EXECUTOR = new SSThreadPoolExecutor(0, Integer.MAX_VALUE,
                                                                0L, TimeUnit.SECONDS,
                                                                new SynchronousQueue<Runnable>(),
                                                                new SSThreadFactory("SS-immediate", SSThreadPriority.HIGH));

    public static final ExecutorService API_EXECUTOR = new SSThreadPoolExecutor(3, 3,
                                                                KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                                                                new LinkedBlockingQueue<Runnable>(),
                                                                new SSThreadFactory("SS-api"));

    public static final ExecutorService COMMON_EXECUTOR = new SSThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                                                                KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                                                                new LinkedBlockingQueue<Runnable>(),
                                                                new SSThreadFactory("SS-common"));

    public static final ExecutorService BACKGROUND_EXECUTOR = Executors.newSingleThreadExecutor(new SSThreadFactory("SS-low", SSThreadPriority.LOW));

}
