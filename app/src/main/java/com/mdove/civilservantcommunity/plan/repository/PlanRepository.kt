package com.mdove.civilservantcommunity.plan.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class PlanRepository {
    private val module = PlanModule()
    private val planCache = PlanCache()

    fun getPlans(): LiveData<Resource<NormalResp<List<List<PlanModuleBean>>>>> {
        return object :
                NetworkBoundResource<NormalResp<List<List<PlanModuleBean>>>, NormalResp<List<List<PlanModuleBean>>>>(
                        AppExecutorsImpl()
                ) {
            override fun saveCallResult(item: NormalResp<List<List<PlanModuleBean>>>) {
                planCache.cachePostResp = item
            }

            override fun shouldFetch(data: NormalResp<List<List<PlanModuleBean>>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<List<List<PlanModuleBean>>>> {
                return MutableLiveData<NormalResp<List<List<PlanModuleBean>>>>().apply {
                    value = planCache.cachePostResp ?: NormalResp<List<List<PlanModuleBean>>>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<List<PlanModuleBean>>>>> {
                return module.getPlans()
            }
        }.asLiveData()
    }
}