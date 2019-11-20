package com.mdove.civilservantcommunity.question

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.FeedReqParams
import com.mdove.civilservantcommunity.feed.bean.MainFeedResp
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
 * Created by zhaojing on 2019-11-19.
 */
class QuestionDetailModule {

    fun reqFeed(feedParams: FeedReqParams): LiveData<ApiResponse<NormalResp<List<MainFeedResp>>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<List<MainFeedResp>>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/play/select_main_feed").buildUpon()
        builder.appendQueryParameter("page", feedParams.page.toString())
        builder.appendQueryParameter("counts", feedParams.counts.toString())
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<List<MainFeedResp>> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<List<MainFeedResp>>(exception = e)
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