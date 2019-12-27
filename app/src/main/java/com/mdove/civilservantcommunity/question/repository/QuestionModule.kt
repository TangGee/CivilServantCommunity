package com.mdove.civilservantcommunity.question.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.question.bean.QuestionDetailResp
import com.mdove.civilservantcommunity.question.bean.QuestionReqParams
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
 * Created by MDove on 2019-11-21.
 */
class QuestionModule {

    fun reqQuestionDetail(params: QuestionReqParams): LiveData<ApiResponse<NormalResp<QuestionDetailResp>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<QuestionDetailResp>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/play/select_question_info").buildUpon()
        builder.appendQueryParameter("qid", params.qid)
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<QuestionDetailResp> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<QuestionDetailResp>(exception = e)
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