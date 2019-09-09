package com.mdove.civilservantcommunity.detailfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedResp
import com.mdove.civilservantcommunity.detailfeed.repository.DetailFeedRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-09.
 */
class DetailFeedViewModel : ViewModel() {
    private val reqLiveData = MutableLiveData<String>()
    private val repository = DetailFeedRepository()

    val mData: LiveData<Resource<NormalResp<DetailFeedResp>>> = Transformations.switchMap(reqLiveData){
        repository.reqDetailFeed(it)
    }

    fun reqDetailFeed(aid: String) {
        reqLiveData.value = aid
    }
}