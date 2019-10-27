package com.mdove.civilservantcommunity.feed.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.feed.repository.MainFeedRepository
import com.mdove.civilservantcommunity.plan.PlanToFeedParams
import com.mdove.civilservantcommunity.punch.PunchHelper
import com.mdove.civilservantcommunity.punch.bean.PunchReq
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
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
    val planParamsLiveData = MutableLiveData<PlanToFeedParams>()

    val mData: LiveData<Resource<List<BaseFeedResp>>> =
        MediatorLiveData<Resource<List<BaseFeedResp>>>().apply {
            addSource(feedData) {
                val temp = mutableListOf<BaseFeedResp>()
                CoroutineScope(FastMain).launch {
                    temp.add(FeedDateResp())
                    temp.add(FeedQuickBtnsResp())
//                    withContext(MDoveBackgroundPool) {
//                        temp.add(
//                            FeedPunchResp(
//                                count = MainDb.db.punchRecordDao().getPunchCounts(),
//                                hasPunch = PunchHelper.hasPunchToday()
//                            )
//                        )
//                    }
                    temp.addAll(it.data?.data?.map { article ->
                        FeedArticleResp(article)
                    } ?: mutableListOf<BaseFeedResp>())
                    value = Resource(it.status, temp, it.exception)
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

            addSource(planParamsLiveData) { params ->
                value = value?.let {
                    Resource(it.status, it.data?.filter {
                        it !is FeedTodayPlanResp
                    }?.toMutableList()?.apply {
                        add(0, FeedTodayPlanResp(params = params))
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