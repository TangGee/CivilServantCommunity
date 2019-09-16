package com.mdove.civilservantcommunity.ugc.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.civilservantcommunity.login.bean.*
import com.mdove.civilservantcommunity.ugc.bean.UGCPostParams
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.apiservice.AppDependsProvider
import com.mdove.dependent.common.gson.GsonProvider
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.network.ServerRespException
import com.mdove.dependent.common.network.toNormaResp
import com.mdove.dependent.common.networkenhance.api.ApiErrorResponse
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.api.ApiSuccessResponse
import com.mdove.dependent.common.utils.fromServerResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by MDove on 2019-09-15.
 */
class UGCModule {
    fun post(params: UGCPostParams): LiveData<ApiResponse<NormalResp<String>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<String>>>()

        val network = AppDependsProvider.networkService
        val url = Uri.parse("${network.host}/art/save").buildUpon().toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.post(url, GsonProvider.getDefaultGson().toJson(params))
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