package com.mdove.civilservantcommunity.detailfeed.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedResp
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
 * Created by MDove on 2019-09-09.
 */
class DetailFeedModule {
    fun reqDetailFeed(aid:String): LiveData<ApiResponse<NormalResp<DetailFeedResp>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<DetailFeedResp>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/art/select_by_aid").buildUpon()
        builder.appendQueryParameter("aid",aid)
        val url =builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<DetailFeedResp> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<DetailFeedResp>(exception = e)
            }
            if (resp.isSuc()) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(ApiErrorResponse(RuntimeException(resp.message)))
            }
        }
        return liveData
    }
}