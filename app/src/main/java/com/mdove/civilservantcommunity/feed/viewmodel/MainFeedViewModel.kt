package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.feed.bean.FeedDataResp
import com.mdove.civilservantcommunity.feed.repository.MainFeedRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by zhaojing on 2019-09-06.
 */
class MainFeedViewModel : ViewModel() {
    private val loadType = MutableLiveData<LoadType>()
    private val repository = MainFeedRepository()

    val data: LiveData<Resource<NormalResp<List<FeedDataResp>>>> =
        Transformations.switchMap(loadType) {
            repository.reqFeed()
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