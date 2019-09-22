package com.mdove.civilservantcommunity.punch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.punch.bean.PunchReq
import com.mdove.civilservantcommunity.punch.repository.PunchRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class PunchViewModel : ViewModel() {
    private val repository = PunchRepository()

    private var punchReq = MutableLiveData<PunchReq>()

    val punchResp: LiveData<Resource<NormalResp<String>>> =
        Transformations.switchMap(punchReq) {
            repository.punch(it)
        }

    fun punch(req: PunchReq) {
        punchReq.value = req
    }
}