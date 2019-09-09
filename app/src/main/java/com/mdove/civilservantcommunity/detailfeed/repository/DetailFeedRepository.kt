package com.mdove.civilservantcommunity.detailfeed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedResp
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-09.
 */
class DetailFeedRepository {
    private val detailModule = DetailFeedModule()
    private val detailCacheModule = DetailFeedCacheModule()

    fun reqDetailFeed(aid: String): LiveData<Resource<NormalResp<DetailFeedResp>>> {
        return object :
            NetworkBoundResource<NormalResp<DetailFeedResp>, NormalResp<DetailFeedResp>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<DetailFeedResp>) {
                detailCacheModule.mCacheFeedResp = item
            }

            override fun shouldFetch(data: NormalResp<DetailFeedResp>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<DetailFeedResp>> {
                return MutableLiveData<NormalResp<DetailFeedResp>>().apply {
                    value = detailCacheModule.mCacheFeedResp ?: NormalResp<DetailFeedResp>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<DetailFeedResp>>> {
                return detailModule.reqDetailFeed(aid)
            }
        }.asLiveData()
    }
}