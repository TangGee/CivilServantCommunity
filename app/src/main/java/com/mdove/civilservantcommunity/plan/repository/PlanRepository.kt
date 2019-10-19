package com.mdove.civilservantcommunity.plan.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.plan.PlanModuleModel
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class PlanRepository {
    private val module = PlanModule()
    private val planCache = PlanCache()

    fun getPlans(): LiveData<Resource<NormalResp<List<PlanModuleModel>>>> {
        return object :
                NetworkBoundResource<NormalResp<List<PlanModuleModel>>, NormalResp<List<PlanModuleModel>>>(
                        AppExecutorsImpl()
                ) {
            override fun saveCallResult(item: NormalResp<List<PlanModuleModel>>) {
                planCache.cachePostResp = item
            }

            override fun shouldFetch(data: NormalResp<List<PlanModuleModel>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<List<PlanModuleModel>>> {
                return MutableLiveData<NormalResp<List<PlanModuleModel>>>().apply {
                    value = planCache.cachePostResp ?: NormalResp<List<PlanModuleModel>>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<PlanModuleModel>>>> {
                return module.getPlans()
            }
        }.asLiveData()
    }
}