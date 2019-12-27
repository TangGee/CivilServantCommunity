package com.mdove.civilservantcommunity.question.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.feed.bean.FeedQuestionFeedResp
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.question.bean.*
import com.mdove.civilservantcommunity.question.repository.CommentRepository
import com.mdove.civilservantcommunity.question.repository.QuestionRepository
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
class QuestionViewModel : ViewModel() {
    private val repository = CommentRepository()
    private val questionRepository = QuestionRepository()

    val questionResp = MutableLiveData<FeedQuestionFeedResp>()
    val questionDetailResp = MutableLiveData<QuestionReqParams>()
    private val proxyAnswerReqParamsLiveData = MutableLiveData<AnswerReqParams>()

    private val reqLiveData = Transformations.switchMap(questionDetailResp) {
        questionRepository.reqQuestionDetail(it)
    }

    val questionDetailLiveData =
        MediatorLiveData<Resource<NormalResp<List<BaseDetailQuestionBean>>>>().apply {
            addSource(reqLiveData) { res ->
                value = res.data?.let {
                    Resource<NormalResp<List<BaseDetailQuestionBean>>>(
                        res.status,
                        NormalResp(
                            res.data?.message ?: "",
                            mutableListOf<BaseDetailQuestionBean>().apply {
                                it.data?.question?.let {
                                    add(it)
                                    add(DetailQuestionSendBean())
                                }
                                it.data?.answers?.let {
                                    addAll(it)
                                }
                                if (res.status == Status.ERROR) {
                                    add(DetailQuestionErrorIconBean())
                                    add(QuestionDetailErrorBean())
                                }
                            },
                            null
                        ),
                        null
                    )
                }
            }
        }

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

    fun buildAnswerCommentSendParams(): AnswerCommentSendParams {
        val question = reqLiveData.value?.data?.data?.question
        return AnswerCommentSendParams(
            question?.qid,
            question?.userInfo?.username,
            null,
            question?.listStyle
        )
    }
}