package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.model.HistoryPlansBaseBean
import com.mdove.civilservantcommunity.plan.model.HistoryPlansEmptyTipsBean
import com.mdove.civilservantcommunity.plan.model.HistoryPlansSinglePlanBean
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-11-14.
 */
class HistoryPlansViewModel : ViewModel() {
    val selectTimeLiveData = MutableLiveData<String>()

    val selectRecordLiveData = MediatorLiveData<List<HistoryPlansBaseBean>>().apply {
        addSource(selectTimeLiveData) { selectTime ->
            CoroutineScope(FastMain).launch {
                value = withContext(MDoveBackgroundPool) {
                    MainDb.db.todayPlansDao().getTodayPlansRecord(selectTime)?.let {
                        it.resp.params.filter {
                            !it.beanSingles.isNullOrEmpty()
                        }.flatMap {
                            it.beanSingles
                        }.map{
                            HistoryPlansSinglePlanBean(
                                it.beanSingle,
                                it.typeSingle,
                                it.statusSingle,
                                it.timeSchedule
                            )
                        }
                    } ?: mutableListOf(HistoryPlansEmptyTipsBean())
                }
            }
        }
    }
}