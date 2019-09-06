/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mdove.dependent.common.network

import android.os.Handler
import android.os.Looper
import com.mdove.dependent.common.networkenhance.executor.AppExecutors
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool

import java.util.concurrent.Executor

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
open class AppExecutorsImpl(
    override val diskIO: Executor,
    override val networkIO: Executor,
    override val mainThread: Executor
) : AppExecutors {

    companion object {
        val inst: AppExecutorsImpl by lazy {
            AppExecutorsImpl()
        }
    }

    constructor() : this(
        MDoveBackgroundPool.executor,
        MDoveApiPool.executor,
        MainThreadExecutor()
    )

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
