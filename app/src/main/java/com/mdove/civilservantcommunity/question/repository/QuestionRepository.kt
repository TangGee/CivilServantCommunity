package com.mdove.civilservantcommunity.question.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.question.bean.AnswerReqParams
import com.mdove.civilservantcommunity.question.bean.CommentReqParams
import com.mdove.civilservantcommunity.question.bean.QuestionDetailResp
import com.mdove.civilservantcommunity.question.bean.QuestionReqParams
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-11-24.
 */
class QuestionRepository {
    private val netModule = QuestionModule()
    private val cacheModule = CacheModule()

    fun reqQuestionDetail(params: QuestionReqParams): LiveData<Resource<NormalResp<QuestionDetailResp>>> {
        return object :
            NetworkBoundResource<NormalResp<QuestionDetailResp>, NormalResp<QuestionDetailResp>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<QuestionDetailResp>) {
                cacheModule.mQuestionDetailCacheResp = item
            }

            override fun shouldFetch(data: NormalResp<QuestionDetailResp>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<QuestionDetailResp>> {
                // 暂不需要
                return MutableLiveData<NormalResp<QuestionDetailResp>>().apply {
                    value = cacheModule.mQuestionDetailCacheResp ?: NormalResp<QuestionDetailResp>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<QuestionDetailResp>>> {
                return netModule.reqQuestionDetail(params)
            }
        }.asLiveData()
    }
}