package com.mdove.civilservantcommunity.feed.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
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
 * Created by MDove on 2019-09-06.
 */
class MainFeedModule {
    fun reqFeed(): LiveData<ApiResponse<NormalResp<List<ArticleResp>>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<List<ArticleResp>>>>()

        val network = AppDependsProvider.networkService
        val url = Uri.parse("${network.host}/art/select_feed").toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<List<ArticleResp>> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<List<ArticleResp>>(exception = e)
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