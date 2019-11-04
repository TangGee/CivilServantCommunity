package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.feed.repository.MainFeedRepository
import com.mdove.civilservantcommunity.plan.PlanModuleStatus
import com.mdove.civilservantcommunity.plan.PlanToFeedParams
import com.mdove.civilservantcommunity.plan.SinglePlanStatus
import com.mdove.civilservantcommunity.plan.SinglePlanType
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
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

    val mData: LiveData<Resource<List<BaseFeedResp>>> =
        MediatorLiveData<Resource<List<BaseFeedResp>>>().apply {
            addSource(feedData) {
                val temp = mutableListOf<BaseFeedResp>()
                CoroutineScope(FastMain).launch {
                    temp.add(FeedDateResp())
                    temp.add(FeedQuickBtnsResp())
                    withContext(MDoveBackgroundPool) {
                        MainDb.db.todayPlansDao().getTodayPlansRecord()?.let { entity ->
                            temp.add(FeedTimeLineFeedTodayPlansTitleResp())
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
                                (this.last()).params.typeSingle = SinglePlanType.LAST_PLAN
                            })
                        }
                    }
                    temp.add(FeedTimeLineFeedTitleResp())
                    temp.addAll(it.data?.data?.map { article ->
                        FeedArticleResp(article)
                    }.apply {
                        (this?.last())?.hideEndLine = true
                    } ?: mutableListOf<BaseFeedResp>())
                    temp.add(FeedPaddingStub())
                    value = Resource(it.status, temp, it.exception)
                }
            }

            addSource(planParamsLiveData) { toFeedParams ->
                value = value?.let {
                    val newData = mutableListOf<BaseFeedResp>()
                    it.data?.forEachIndexed { index, baseFeedResp ->
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
}

enum class LoadType {
    NORMAL,
    REFRESH,
    LOAD_MORE
}