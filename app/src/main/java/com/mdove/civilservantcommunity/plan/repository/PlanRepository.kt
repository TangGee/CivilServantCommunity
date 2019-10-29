package com.mdove.civilservantcommunity.plan.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class PlanRepository {
    private val module = PlanModule()
    private val planCache = PlanCache()

    fun getPlans(): LiveData<Resource<NormalResp<List<List<SinglePlanBean>>>>> {
        return object :
                NetworkBoundResource<NormalResp<List<List<SinglePlanBean>>>, NormalResp<List<List<SinglePlanBean>>>>(
                        AppExecutorsImpl()
                ) {
            override fun saveCallResult(item: NormalResp<List<List<SinglePlanBean>>>) {
                planCache.mCachePostResp = item
            }

            override fun shouldFetch(data: NormalResp<List<List<SinglePlanBean>>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<List<List<SinglePlanBean>>>> {
                return MutableLiveData<NormalResp<List<List<SinglePlanBean>>>>().apply {
                    value = planCache.mCachePostResp ?: NormalResp<List<List<SinglePlanBean>>>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<List<SinglePlanBean>>>>> {
                return module.getPlans()
            }
        }.asLiveData()
    }
}