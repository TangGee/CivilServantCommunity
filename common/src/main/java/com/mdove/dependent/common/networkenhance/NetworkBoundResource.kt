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

package com.mdove.dependent.common.networkenhance

import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mdove.dependent.common.networkenhance.api.ApiEmptyResponse
import com.mdove.dependent.common.networkenhance.api.ApiErrorResponse
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.api.ApiSuccessResponse
import com.mdove.dependent.common.networkenhance.executor.AppExecutors
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <FinalType> 最后呈现给业务的数据结构
 * @param <MiddleType> 网络数据，如果没有变换则 MiddleType == FinalType
 *
 * 适用全量数据查询和更新
 * 不想破坏方法签名，所以增加requestParam
 *
</RequestType></ResultType> */
abstract class NetworkBoundResource<FinalType, MiddleType>
@MainThread constructor(private val appExecutors: AppExecutors, var requestParam: Any?= null) {

    private val result = MediatorLiveData<Resource<FinalType>>()

    init {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            result.value = Resource.loading(null)
        } else {
            result.postValue(Resource.loading(null))
        }
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<FinalType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<FinalType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO.execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread.execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread.execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.exception, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<FinalType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<MiddleType>) = response.data

    @WorkerThread
    protected abstract fun saveCallResult(item: MiddleType)

    @MainThread
    protected abstract fun shouldFetch(data: FinalType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<FinalType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<MiddleType>>
}
