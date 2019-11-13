package com.mdove.civilservantcommunity.plan.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.plan.model.SinglePlanBeanWrapper
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PlanRepository {
    private val module = PlanModule()
    private val planCache = PlanCache()

    fun getPlans(): LiveData<Resource<NormalResp<List<List<SinglePlanBeanWrapper>>>>> {
        return object :
            NetworkBoundResource<NormalResp<List<List<SinglePlanBeanWrapper>>>, NormalResp<List<List<SinglePlanBeanWrapper>>>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<List<List<SinglePlanBeanWrapper>>>) {
                planCache.mCachePostResp = item
            }

            override fun shouldFetch(data: NormalResp<List<List<SinglePlanBeanWrapper>>>?): Boolean {
                return data?.data.isNullOrEmpty()
            }

            override fun loadFromDb(): LiveData<NormalResp<List<List<SinglePlanBeanWrapper>>>> {
                val cacheLiveData = MutableLiveData<NormalResp<List<List<SinglePlanBeanWrapper>>>>()
                CoroutineScope(MDoveBackgroundPool).launch {
                    (planCache.getTodayPlans() ?: planCache.mCachePostResp)?.let {
                        cacheLiveData.postValue(it)
                    } ?: also{
                        cacheLiveData.postValue(NormalResp<List<List<SinglePlanBeanWrapper>>>())
                    }
                }
                return cacheLiveData
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<List<SinglePlanBeanWrapper>>>>> {
                return module.getPlans()
            }
        }.asLiveData()
    }
}