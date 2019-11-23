package com.mdove.civilservantcommunity.question.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.question.bean.AnswerReqParams
import com.mdove.civilservantcommunity.question.bean.CommentReqParams
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-11-21.
 */
class CommentRepository {
    private val netModule = CommentModule()
    private val cacheModule = CommentCacheModule()

    fun saveAnswer(params: AnswerReqParams): LiveData<Resource<NormalResp<String>>> {
        return object :
            NetworkBoundResource<NormalResp<String>, NormalResp<String>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<String>) {
                cacheModule.mAnswerCacheResp = item
            }

            override fun shouldFetch(data: NormalResp<String>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<String>> {
                // 暂不需要
                return MutableLiveData<NormalResp<String>>().apply {
                    value = cacheModule.mAnswerCacheResp ?: NormalResp<String>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<String>>> {
                return netModule.saveAnswer(params)
            }
        }.asLiveData()
    }

    fun saveComment(params: CommentReqParams): LiveData<Resource<NormalResp<String>>> {
        return object :
            NetworkBoundResource<NormalResp<String>, NormalResp<String>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<String>) {
                cacheModule.mQuestionCacheResp = item
            }

            override fun shouldFetch(data: NormalResp<String>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<String>> {
                // 暂不需要
                return MutableLiveData<NormalResp<String>>().apply {
                    value = cacheModule.mQuestionCacheResp ?: NormalResp<String>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<String>>> {
                return netModule.saveComment(params)
            }
        }.asLiveData()
    }
}