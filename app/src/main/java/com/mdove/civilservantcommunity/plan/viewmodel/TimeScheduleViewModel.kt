package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.model.TimeScheduleParams

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleViewModel : ViewModel() {
    val paramsLiveData = MutableLiveData<TimeScheduleParams>()

    val plansLiveData = Transformations.map(paramsLiveData){
        it.data
    }
}