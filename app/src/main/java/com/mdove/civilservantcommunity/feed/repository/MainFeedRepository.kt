package com.mdove.civilservantcommunity.feed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.FeedReqParams
import com.mdove.civilservantcommunity.feed.bean.MainFeedResp
import com.mdove.civilservantcommunity.feed.bean.NormalRespWrapper
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
    private var curPage = 1
    fun reqFeed(count: Int, forceRefresh: Boolean): LiveData<Resource<NormalRespWrapper<List<MainFeedResp>>>> {
        return object :
            NetworkBoundResource<NormalRespWrapper<List<MainFeedResp>>, NormalRespWrapper<List<MainFeedResp>>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalRespWrapper<List<MainFeedResp>>) {
                curPage += 1
                mainFeedCacheModule.mCacheFeedResp = item
            }

            override fun shouldFetch(data: NormalRespWrapper<List<MainFeedResp>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalRespWrapper<List<MainFeedResp>>> {
                // 暂不需要
                return MutableLiveData<NormalRespWrapper<List<MainFeedResp>>>().apply {
                    value = mainFeedCacheModule.mCacheFeedResp ?: NormalRespWrapper<List<MainFeedResp>>(NormalResp())
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalRespWrapper<List<MainFeedResp>>>> {
                return mainFeedModule.reqFeed(FeedReqParams(if (forceRefresh) 1 else curPage, count))
            }
        }.asLiveData()
    }
}