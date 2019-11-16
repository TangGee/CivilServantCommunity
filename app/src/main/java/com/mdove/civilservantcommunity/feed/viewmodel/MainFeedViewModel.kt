package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.feed.repository.MainFeedRepository
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.utils.TimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedViewModel : ViewModel() {
    private val loadType = MutableLiveData<LoadType>()
    private val repository = MainFeedRepository()

    private val feedData: LiveData<Resource<NormalResp<List<ArticleResp>>>> =
        Transformations.switchMap(loadType) {
            repository.reqFeed()
        }


    val punchResp = MutableLiveData<Boolean>()
    // 从编辑计划页面跳转过来
    val planParamsLiveData = MutableLiveData<PlanToFeedParams>()
    val checkTodayPlanLiveData = MutableLiveData<FeedTodayPlansCheckParams>()
    val timeScheduleToFeedLiveData = MutableLiveData<TimeScheduleParams>()
    val editNewPlanToFeedLiveData = MutableLiveData<FeedTimeLineFeedTodayPlansResp>()

    val mData: LiveData<Resource<List<BaseFeedResp>>> =
        MediatorLiveData<Resource<List<BaseFeedResp>>>().apply {
            addSource(feedData) {
                val temp = mutableListOf<BaseFeedResp>()
                CoroutineScope(FastMain).launch {
                    temp.add(FeedDateResp(System.currentTimeMillis()))
                    temp.add(FeedQuickEditNewPlanResp())
                    temp.add(FeedQuickBtnsResp())
                    withContext(MDoveBackgroundPool) {
                        temp.add(FeedTimeLineFeedTodayPlansTitleResp())
                        MainDb.db.todayPlansDao().getTodayPlansRecord(TimeUtils.getDateFromSQL())?.let { entity ->
                            if (entity.resp.params.isNotEmpty()) {
                                temp.addAll(entity.resp.params.flatMap { planModule ->
                                    planModule.beanSingles.map {
                                        FeedTimeLineFeedTodayPlansResp(
                                            entity.id,
                                            entity.date,
                                            entity.sucDate,
                                            entity.createDate ?: TimeUtils.getDateFromSQL(),
                                            it
                                        )
                                    }
                                }.apply {
                                    (this.lastOrNull())?.params?.typeSingle =
                                        SinglePlanType.LAST_PLAN
                                })
                            } else {
                                temp.add(FeedTimeLineFeedTodayPlansTipsTitleResp())
                            }
                        } ?: also {
                            temp.add(FeedTimeLineFeedTodayPlansTipsTitleResp())
                        }
                    }
                    temp.add(FeedTimeLineFeedTitleResp())
                    if (it.status == Status.ERROR) {
                        temp.add(FeedNetworkErrorTitleResp())
                    } else {
                        temp.addAll(it.data?.data?.map { article ->
                            FeedArticleResp(article)
                        }.apply {
                            (this?.last())?.hideEndLine = true
                        } ?: mutableListOf<BaseFeedResp>())
                    }
                    temp.add(FeedPaddingStub())
                    value = Resource(
                        if (it.status == Status.ERROR) Status.SUCCESS else it.status,
                        temp,
                        it.exception
                    )
                }
            }

            // 主Feed直接插入一个计划
            addSource(editNewPlanToFeedLiveData) { insertResp ->
                value = value?.let {
                    val tempData = mutableListOf<BaseFeedResp>()
                    it.data?.filter {
                        !(it is FeedTimeLineFeedTodayPlansTipsTitleResp)
                    }?.forEach { resp ->
                        if (resp is FeedTimeLineFeedTodayPlansTitleResp) {
                            tempData.add(resp)
                            tempData.add(insertResp)
                        } else {
                            tempData.add(resp)
                        }
                    }
                    Resource(it.status, tempData, it.exception)
                }
            }

            addSource(timeScheduleToFeedLiveData) { params ->
                value = value?.let { res ->
                    Resource(res.status, data = res.data?.let {
                        it.map { feedResp ->
                            if (feedResp is FeedTimeLineFeedTodayPlansResp) {
                                params.data.find {
                                    it.data.moduleId == feedResp.params.beanSingle.moduleId &&
                                            it.data.content == feedResp.params.beanSingle.content
                                }?.let {
                                    feedResp.copy(
                                        params = feedResp.params.copy(
                                            timeSchedule = it.timeSchedule,
                                            statusSingle = SinglePlanStatus.CONTENT_CHANGE
                                        )
                                    )
                                } ?: feedResp
                            } else {
                                feedResp
                            }
                        }
                    } ?: mutableListOf<BaseFeedResp>(), exception = res.exception)
                }
            }

            addSource(planParamsLiveData) { toFeedParams ->
                value = value?.let {
                    val newData = mutableListOf<BaseFeedResp>()
                    it.data?.filter {
                        it !is FeedTimeLineFeedTodayPlansTitleResp && it !is FeedTimeLineFeedTodayPlansResp
                                && it !is FeedTimeLineFeedTodayPlansTipsTitleResp
                    }?.forEachIndexed { index, baseFeedResp ->
                        // 找到QuickBtns，然后在其下边增加我的今日计划
                        if (baseFeedResp is FeedQuickBtnsResp) {
                            newData.add(baseFeedResp)
                            newData.add(FeedTimeLineFeedTodayPlansTitleResp())
                            newData.addAll(toFeedParams.data.filter {
                                it.moduleStatus != PlanModuleStatus.DELETE
                            }.flatMap { planModule ->
                                planModule.beanSingles.filter {
                                    it.statusSingle != SinglePlanStatus.DELETE
                                }.map {
                                    FeedTimeLineFeedTodayPlansResp(
                                        toFeedParams.entityId,
                                        toFeedParams.insertDate,
                                        toFeedParams.sucDate,
                                        toFeedParams.createDate,
                                        it
                                    )
                                }
                            }.apply {
                                (this.last()).params.typeSingle = SinglePlanType.LAST_PLAN
                            })
                        } else {
                            newData.add(baseFeedResp)
                        }
                    }
                    Resource(it.status, newData, it.exception)
                }
            }

            addSource(punchResp) {
                if (it) {
                    value?.let { resource ->
                        value = Resource(resource.status, value?.data?.map { baseResp ->
                            if (baseResp is FeedPunchResp) {
                                FeedPunchResp(baseResp.count + 1, true)
                            } else {
                                baseResp
                            }
                        }, resource.exception)
                    }
                }
            }

            addSource(checkTodayPlanLiveData) { params ->
                value = value?.let {
                    Resource(it.status, it.data?.map {
                        if ((it as? FeedTimeLineFeedTodayPlansResp)?.params?.beanSingle?.content == params.resp.params.beanSingle.content) {
                            params.resp.copy(
                                params = params.resp.params.copy(
                                    statusSingle = if (params.select)
                                        SinglePlanStatus.SELECT
                                    else
                                        SinglePlanStatus.NORMAL
                                )
                            )
                        } else {
                            it
                        }
                    }, it.exception)
                }
            }
        }


    fun reqFeed() {
        loadType.value = LoadType.NORMAL
    }

    fun createTimeScheduleParams(): TimeScheduleParams {
        return TimeScheduleParams(mData.value?.data?.let {
            it.filterIsInstance<FeedTimeLineFeedTodayPlansResp>().map {
                TimeSchedulePlansParams(
                    it.params.beanSingle,
                    TimeSchedulePlansStatus.SHOW,
                    it.params.timeSchedule
                )
            }
        } ?: mutableListOf())
    }
}

enum class LoadType {
    NORMAL,
    REFRESH,
    LOAD_MORE
}