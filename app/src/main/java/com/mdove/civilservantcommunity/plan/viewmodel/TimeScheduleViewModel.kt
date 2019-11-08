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
    // 被添加Plan，需要从Rlv中移除
    val removeSinglePlanBean = MutableLiveData<SinglePlanBean>()
    // Rlv被touch的View的显示与否
    val changeSinglePlanBean =
        MutableLiveData<Pair<SinglePlanBean, TimeSchedulePlansStatus>>()

    val plansLiveData = MediatorLiveData<List<TimeSchedulePlansParams>>().apply {
        addSource(paramsLiveData) {
            value = it.data.map {
                TimeSchedulePlansParams(
                    data = it.params.beanSingle,
                    status = TimeSchedulePlansStatus.SHOW
                )
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
                    && it.data.content == change.first.content) {
                    it.copy(status = change.second)
                } else {
                    it
                }
            }
        }
    }
}