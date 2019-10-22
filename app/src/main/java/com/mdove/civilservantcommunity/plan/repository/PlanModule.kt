package com.mdove.civilservantcommunity.plan.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.apiservice.AppDependsProvider
import com.mdove.dependent.common.gson.GsonArrayHelper.fromJsonArray
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.api.ApiErrorResponse
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.api.ApiSuccessResponse
import com.mdove.dependent.common.utils.fromServerResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by MDove on 2019-09-15.
 */
class PlanModule {

    fun getPlans(): LiveData<ApiResponse<NormalResp<List<List<PlanModuleBean>>>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<List<List<PlanModuleBean>>>>>()

        val network = AppDependsProvider.networkService
        val url = Uri.parse("${network.host}/plan/select_plan").buildUpon().toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient[url]
                val data: NormalResp<List<List<PlanModuleBean>>> = fromJsonArray<List<PlanModuleBean>>(json)
                data
            } catch (e: Exception) {
                NormalResp<List<List<PlanModuleBean>>>(exception = e)
            }
            if (resp.exception == null) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(
                    ApiErrorResponse(
                        resp.exception ?: RuntimeException("unknown_error")
                    )
                )
            }
        }
        return liveData
    }
}