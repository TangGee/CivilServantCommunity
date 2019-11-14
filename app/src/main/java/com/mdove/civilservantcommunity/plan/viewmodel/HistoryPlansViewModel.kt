package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.model.PlanModuleBean
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

    val selectRecordLiveData = MediatorLiveData<List<PlanModuleBean>?>().apply {
        addSource(selectTimeLiveData) { selectTime ->
            CoroutineScope(FastMain).launch {
                value = withContext(MDoveBackgroundPool) {
                    MainDb.db.todayPlansDao().getTodayPlansRecord(selectTime)?.let {
                        it.resp.params.filter {
                            !it.beanSingles.isNullOrEmpty()
                        }
                    }
                }
            }
        }
    }
}