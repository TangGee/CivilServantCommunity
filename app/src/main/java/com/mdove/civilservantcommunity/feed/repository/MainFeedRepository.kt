package com.mdove.civilservantcommunity.feed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.FeedReqParams
import com.mdove.civilservantcommunity.feed.bean.MainFeedResp
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
    fun reqFeed(feedParams: FeedReqParams): LiveData<Resource<NormalResp<List<MainFeedResp>>>> {
        return object :
            NetworkBoundResource<NormalResp<List<MainFeedResp>>, NormalResp<List<MainFeedResp>>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<List<MainFeedResp>>) {
                mainFeedCacheModule.mCacheFeedResp = item
            }

            override fun shouldFetch(data: NormalResp<List<MainFeedResp>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<List<MainFeedResp>>> {
                // 暂不需要
                return MutableLiveData<NormalResp<List<MainFeedResp>>>().apply {
                    value = mainFeedCacheModule.mCacheFeedResp ?: NormalResp<List<MainFeedResp>>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<MainFeedResp>>>> {
                return mainFeedModule.reqFeed(feedParams)
            }
        }.asLiveData()
    }
}