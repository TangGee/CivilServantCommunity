package com.mdove.civilservantcommunity.question.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.feed.bean.FeedQuestionFeedResp
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.question.bean.AnswerReqParams
import com.mdove.civilservantcommunity.question.repository.CommentRepository
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.common.utils.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-11-21.
 */
class CommentViewModel : ViewModel() {
    private val repository = CommentRepository()

    val questionResp = MutableLiveData<FeedQuestionFeedResp>()
    private val proxyAnswerReqParamsLiveData = MutableLiveData<AnswerReqParams>()

    val saveAnswerLiveData = MediatorLiveData<Resource<NormalResp<String>>>().apply {
        addSource(Transformations.switchMap(proxyAnswerReqParamsLiveData) {
            repository.saveAnswer(it)
        }) {
            value = it
        }
        addSource(questionResp) { questionResp ->
            CoroutineScope(MDoveApiPool).launch {
                MainDb.db.todayPlansDao().getTodayPlansRecord()?.let {
                    withContext(FastMain) {
                        buildAnswerReqParams(questionResp, it)?.let {
                            proxyAnswerReqParamsLiveData.value = it
                        }
                    }
                } ?: also {
                    withContext(FastMain) {
                        // 今日无数据
                        value =
                            Resource(
                                Status.ERROR,
                                NormalResp("今日没有计划，没办法一建分享。", "", null, 1),
                                null
                            )
                    }
                }
            }
        }
    }

    private fun buildAnswerReqParams(
        question: FeedQuestionFeedResp,
        entity: TodayPlansEntity
    ): AnswerReqParams? {
        return AppConfig.getUserInfo()?.let { userInfo ->
            question.question.qid?.let {
                AnswerReqParams(userInfo, it, entity.toJson(), "4")
            }
        }
    }

//    private fun buildCommentReqParams(question: FeedQuestionFeedResp, entity: TodayPlansEntity) {
//        return CommentReqParams(question.question.qid)
//    }
}