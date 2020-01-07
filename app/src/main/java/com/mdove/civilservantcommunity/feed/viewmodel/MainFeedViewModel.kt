package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_QUICK_BTNS
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_TITLE
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.feed.repository.MainFeedRepository
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.civilservantcommunity.setting.utils.HideRecorder
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
    companion object {
        val NORMAL_CARD_TYPES = mutableListOf(
            MainFeedAdapter.TYPE_FEED_DEV,
            MainFeedAdapter.TYPE_FEED_DATE,
            TYPE_FEED_QUICK_BTNS,
            TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_TITLE
        )
    }

    private val loadType = MutableLiveData<LoadType>()
    private val repository = MainFeedRepository()

    private val feedData: LiveData<Resource<NormalRespWrapper<List<MainFeedResp>>>> =
        Transformations.switchMap(loadType) {
            repository.reqFeed(6, it == LoadType.NORMAL || it == LoadType.REFRESH)
        }


    val punchResp = MutableLiveData<Boolean>()
    // 从编辑计划页面跳转过来
    val planParamsLiveData = MutableLiveData<PlanToFeedParams>()
    val checkTodayPlanLiveData = MutableLiveData<FeedTodayPlansCheckParams>()
    val timeScheduleToFeedLiveData = MutableLiveData<TimeScheduleParams>()
    val editNewPlanToFeedLiveData = MutableLiveData<FeedTimeLineFeedTodayPlansResp>()
    val appConfigLiveData = MutableLiveData<UserInfo?>()
    // 一键应用昨天未完成的任务
    val applyOldPlansLiveData = MutableLiveData<String>()
    val hideLiveData = MutableLiveData<HideRecordParams>()

    val mData: LiveData<Resource<List<BaseFeedResp>>> =
        MediatorLiveData<Resource<List<BaseFeedResp>>>().apply {
            // 隐藏某个可以隐藏的卡片
            addSource(hideLiveData) { params ->
                value?.let {
                    val realData = if (params.isHide) {
                        value?.data?.filter {
                            it.type != params.type
                        }
                    } else {
                        it.data?.let { originData ->
                            originData.filter {
                                !NORMAL_CARD_TYPES.contains(it.type)
                            }.toMutableList().apply {
                                addAll(0, generateNormalCard())
                            }
                        }
                    }
                    value = Resource(it.status, realData, it.exception)
                }
            }

            addSource(appConfigLiveData) { userInfo ->
                value?.let {
                    value = Resource(it.status, value?.data?.map {
                        if (it is FeedDateResp) {
                            it.copy(name = userInfo?.username)
                        } else {
                            it
                        }
                    }, it.exception)
                }
            }
            addSource(feedData) { res ->
                val oldData = value?.data?.filter {
                    it is FeedArticleFeedResp || it is FeedQuestionFeedResp
                }?.takeIf {
                    it.isNotEmpty()
                }
                val temp = mutableListOf<BaseFeedResp>()
                CoroutineScope(FastMain).launch {
                    withContext(MDoveBackgroundPool) {
                        temp.addAll(generateNormalCard())
                        val showApply =
                            MainDb.db.todayPlansDao().getTodayPlansRecord(TimeUtils.getDateFromSQLYesterday()) != null
                        MainDb.db.todayPlansDao().getTodayPlansRecord(TimeUtils.getDateFromSQL())?.let { entity ->
                            if (entity.resp.params.isNotEmpty()) {
                                temp.add(FeedEncourageTipsResp())
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
                                    (this.lastOrNull())?.hideEndLine = true
                                })
                            } else {
                                temp.add(FeedTimeLineFeedTodayPlansTipsTitleResp(showApply = showApply))
                            }
                        } ?: also {
                            temp.add(FeedTimeLineFeedTodayPlansTipsTitleResp(showApply = showApply))
                        }
                    }
                    temp.add(FeedTimeLineFeedTitleResp())
                    when {
                        res.status == Status.ERROR -> {
                            oldData?.let {
                                temp.addAll(it)
                            }
                            temp.add(FeedNetworkErrorTitleResp())
                        }
                        res.status == Status.SUCCESS -> {
                            res.data?.data?.data?.mapNotNull { article ->
                                article.toMainFeedResp()
                            }?.takeIf {
                                it.isNotEmpty()
                            }?.let { newData ->
                                oldData?.forEach {
                                    it.hideEndLine = false
                                    if (res.data?.isLoadMore == true) {
                                        temp.add(it)
                                    }
                                }
                                (newData.last()).hideEndLine = true
                                temp.addAll(newData)
                                temp.add(FeedLoadMoreResp())
                            } ?: also {
                                addDefaultList(oldData, temp)
                            }
                        }
                        else -> {
                            addDefaultList(oldData, temp)
                        }
                    }
                    temp.add(FeedBottomPaddingResp())
                    value = Resource(
                        if (res.status == Status.ERROR) Status.SUCCESS else res.status,
                        temp,
                        res.exception
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

            addSource(applyOldPlansLiveData) {
                CoroutineScope(FastMain).launch {
                    val yesterdayPlans = withContext(MDoveBackgroundPool) {
                        val yesterday = MainDb.db.todayPlansDao()
                            .getTodayPlansRecord(TimeUtils.getDateFromSQLYesterday())
                        yesterday?.let {
                            // 把老的计划插入到今天的记录中，先过滤已完成的
                            val insertResp = it.resp.copy(params = it.resp.params.map {
                                it.copy(beanSingles = it.beanSingles.filter {
                                    it.statusSingle != SinglePlanStatus.SELECT
                                })
                            })
                            MainDb.db.todayPlansDao().insert(
                                TodayPlansEntity(
                                    date = System.currentTimeMillis(),
                                    createDate = TimeUtils.getDateFromSQL(),
                                    sucDate = null,
                                    resp = insertResp
                                )
                            )
                        }
                        yesterday
                    }
                    yesterdayPlans?.let { entity ->
                        value = value?.let { res ->
                            val tempData = mutableListOf<BaseFeedResp>()
                            res.data?.filter {
                                it !is FeedTimeLineFeedTodayPlansTipsTitleResp
                            }?.forEach { resp ->
                                if (resp is FeedTimeLineFeedTodayPlansTitleResp) {
                                    tempData.add(resp)
                                    tempData.addAll(entity.resp.params.flatMap {
                                        it.beanSingles
                                    }.filter {
                                        it.statusSingle != SinglePlanStatus.SELECT
                                    }.map {
                                        FeedTimeLineFeedTodayPlansResp(
                                            entity.id,
                                            entity.date,
                                            entity.sucDate,
                                            entity.createDate ?: TimeUtils.getDateFromSQL(),
                                            it
                                        )
                                    }.apply {
                                        last().hideEndLine = true
                                    })
                                } else {
                                    tempData.add(resp)
                                }
                            }
                            Resource(res.status, tempData, res.exception)
                        }
                    }
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
                                (this.last()).hideEndLine = true
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

    private fun addDefaultList(
        oldData: List<BaseFeedResp>?,
        temp: MutableList<BaseFeedResp>
    ) {
        oldData?.forEachIndexed { index, resp ->
            if (index == oldData.size - 1) {
                resp.hideEndLine = true
                temp.add(resp)
                temp.add(FeedNoContentResp())
            } else {
                resp.hideEndLine = false
                temp.add(resp)
            }
        }
    }

    fun reqFeed(type: LoadType = LoadType.NORMAL) {
        loadType.value = type
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

    // 创建通用卡片。（所有通用卡片的type放在一个集合中，用于filter）
    private fun generateNormalCard(): List<BaseFeedResp> {
        return mutableListOf<BaseFeedResp>().apply {
            add(FeedDateResp(System.currentTimeMillis(), AppConfig.getUserInfo()?.username))
            val hideTypes = HideRecorder.getHideRecordTypes()
            hideTypes?.find {
                it == MainFeedAdapter.TYPE_FEED_DEV
            } ?: also {
                add(FeedDevTitleResp())
            }
            add(FeedQuickEditNewPlanResp())
            hideTypes?.find {
                it == MainFeedAdapter.TYPE_FEED_QUICK_BTNS
            } ?: also {
                add(FeedQuickBtnsResp())
            }
            add(FeedTimeLineFeedTodayPlansTitleResp())
        }
    }
}

data class HideRecordParams(val type: Int, val isHide: Boolean)
enum class LoadType {
    NORMAL,
    REFRESH,
    LOAD_MORE
}