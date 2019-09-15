package com.mdove.civilservantcommunity.punch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.punch.bean.PunchParams
import com.mdove.dependent.apiservice.AppDependsProvider
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.api.ApiErrorResponse
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.api.ApiSuccessResponse
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.common.utils.fromServerResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by MDove on 2019-09-15.
 */
class PunchModule {

    fun punch(params: PunchParams): LiveData<ApiResponse<NormalResp<String>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<String>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/user/call_last_time").buildUpon()
        builder.appendQueryParameter("uid", params.uid)
        builder.appendQueryParameter("call_time", params.callTime.toString())
        val url = builder.toString()
        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<String> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<String>(exception = e)
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