package com.mdove.civilservantcommunity.feed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
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
    fun reqFeed(): LiveData<Resource<NormalResp<List<ArticleResp>>>> {
        return object :
            NetworkBoundResource<NormalResp<List<ArticleResp>>, NormalResp<List<ArticleResp>>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<List<ArticleResp>>) {
                mainFeedCacheModule.mCacheFeedResp = item
            }

            override fun shouldFetch(data: NormalResp<List<ArticleResp>>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<List<ArticleResp>>> {
                // 暂不需要
                return MutableLiveData<NormalResp<List<ArticleResp>>>().apply {
                    value = mainFeedCacheModule.mCacheFeedResp ?: NormalResp<List<ArticleResp>>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<List<ArticleResp>>>> {
                return mainFeedModule.reqFeed()
            }
        }.asLiveData()
    }
}