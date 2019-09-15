package com.mdove.civilservantcommunity.punch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.punch.bean.PunchParams
import com.mdove.civilservantcommunity.punch.repository.PunchRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class PunchViewModel : ViewModel() {
    private val repository =
            PunchRepository()

    private var punchReq = MutableLiveData<PunchParams>()

    val punchResp: LiveData<Resource<NormalResp<String>>> =
            Transformations.switchMap(punchReq) {
                repository.punch(it)
            }

    fun punch(params: PunchParams) {
        punchReq.value = params
    }
}