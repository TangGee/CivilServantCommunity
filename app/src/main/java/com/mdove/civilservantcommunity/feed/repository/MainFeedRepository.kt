package com.mdove.civilservantcommunity.feed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.FeedDataResp
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedRepository {
    private val mainFeedModule = MainFeedModule()
    private val mainFeedCacheModule = MainFeedCacheModule()
    fun reqFeed(): LiveData<Resource<NormalResp<List<FeedDataResp>>>> {
        return object :
            NetworkBoundResource<NormalResp<List<FeedDataResp>>, NormalResp<List<FeedDataResp>>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<List<FeedDataResp>>) {
                mainFeedCacheModule.cacheFeedResp = item
            }

            override fun shouldFetch(data: NormalResp<List<FeedDataResp>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<List<FeedDataResp>>> {
                // 暂不需要
                return MutableLiveData<NormalResp<List<FeedDataResp>>>().apply {
                    value = mainFeedCacheModule.cacheFeedResp ?: NormalResp<List<FeedDataResp>>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<FeedDataResp>>>> {
                return mainFeedModule.reqFeed()
            }
        }.asLiveData()
    }
}