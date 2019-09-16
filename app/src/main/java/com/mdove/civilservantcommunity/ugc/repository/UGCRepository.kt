package com.mdove.civilservantcommunity.ugc.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.ugc.bean.UGCPostParams
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-15.
 */
class UGCRepository {
    private val ugcModule = UGCModule()
    private val ugcCache = UGCCache()

    fun post(params: UGCPostParams): LiveData<Resource<NormalResp<String>>> {
        return object :
            NetworkBoundResource<NormalResp<String>, NormalResp<String>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<String>) {
                ugcCache.cachePostResp = item
            }

            override fun shouldFetch(data: NormalResp<String>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<String>> {
                return MutableLiveData<NormalResp<String>>().apply {
                    value = ugcCache.cachePostResp ?: NormalResp<String>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<String>>> {
                return ugcModule.post(params)
            }
        }.asLiveData()
    }
}