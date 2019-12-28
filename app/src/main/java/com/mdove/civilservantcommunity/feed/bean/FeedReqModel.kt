package com.mdove.civilservantcommunity.feed.bean

import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-11-17.
 */
data class FeedReqParams(val page: Int = 1, val counts: Int = 10, val isLoadMore: Boolean = page != 1)

data class NormalRespWrapper<T>(val data: NormalResp<T>, val isLoadMore: Boolean = false)