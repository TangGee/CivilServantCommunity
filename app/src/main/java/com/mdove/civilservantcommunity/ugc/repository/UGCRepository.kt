package com.mdove.civilservantcommunity.ugc.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.civilservantcommunity.login.bean.*
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

    fun post(loginInfoParams: RegisterInfoParams): LiveData<Resource<NormalResp<RegisterDataResp>>> {
        return object :
            NetworkBoundResource<NormalResp<RegisterDataResp>, NormalResp<RegisterDataResp>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<RegisterDataResp>) {
                ugcCache.cacheRegisterResp = item
            }

            override fun shouldFetch(data: NormalResp<RegisterDataResp>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<RegisterDataResp>> {
                return MutableLiveData<NormalResp<RegisterDataResp>>().apply {
                    value = ugcCache.cacheRegisterResp ?: NormalResp<RegisterDataResp>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<RegisterDataResp>>> {
                return ugcModule.register(loginInfoParams)
            }
        }.asLiveData()
    }
}