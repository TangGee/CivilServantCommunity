package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.plan.model.TimeScheduleParams
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansParams
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansStatus

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleViewModel : ViewModel() {
    val paramsLiveData = MutableLiveData<TimeScheduleParams>()
    // 添加到Time块中Plan，需要从Rlv中移除
    val removeSinglePlanBean = MutableLiveData<SinglePlanBean>()
    // 从Time块中释放到Rlv中
    val releasSinglePlanBean = MutableLiveData<SinglePlanBean>()
    // Rlv被touch的View的显示与否
    val changeSinglePlanBean =
        MutableLiveData<Pair<SinglePlanBean, TimeSchedulePlansStatus>>()

    private val handlePlansData = mutableListOf<TimeSchedulePlansParams>()

    val plansLiveData = MediatorLiveData<List<TimeSchedulePlansParams>>().apply {
        addSource(paramsLiveData) {
            value = it.data.map {
                handlePlansData.add(it)
                it
            }
        }

        addSource(releasSinglePlanBean) { release ->
            value = value?.let {
                val newData = mutableListOf<TimeSchedulePlansParams>()
                newData.add(TimeSchedulePlansParams(release, TimeSchedulePlansStatus.SHOW))
                newData.addAll(it)
                newData
            }
        }

        addSource(removeSinglePlanBean) { remove ->
            value = value?.filterNot {
                it.data.moduleId == remove.moduleId &&
                        it.data.content == remove.content
            }
        }

        addSource(changeSinglePlanBean) { change ->
            value = value?.map {
                if (it.data.moduleId == change.first.moduleId
                    && it.data.content == change.first.content
                ) {
                    it.copy(status = change.second)
                } else {
                    it
                }
            }
        }
    }


    fun putOrderTime(data: SinglePlanBean, time: Pair<Long, Long>) {
        handlePlansData.forEach {
            if (it.data.moduleId == data.moduleId && it.data.content == data.content) {
                it.timeSchedule = time
            }
        }
    }

    fun createTimeSchedulePlansToFeed(): TimeScheduleParams {
        return TimeScheduleParams(handlePlansData)
    }
}