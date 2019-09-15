package com.mdove.civilservantcommunity.punch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.punch.bean.PunchParams
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-15.
 */
class PunchRepository {
    private val punchModule = PunchModule()
    private val punchCache = PunchCache()

    fun punch(params: PunchParams): LiveData<Resource<NormalResp<String>>> {
        return object :
                NetworkBoundResource<NormalResp<String>, NormalResp<String>>(
                        AppExecutorsImpl()
                ) {
            override fun saveCallResult(item: NormalResp<String>) {
                punchCache.setCache(params, item)
            }

            override fun shouldFetch(data: NormalResp<String>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<String>> {
                return MutableLiveData<NormalResp<String>>().apply {
                    value = punchCache.cacheResp ?: NormalResp<String>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<String>>> {
                return punchModule.punch(params)
            }
        }.asLiveData()
    }
}