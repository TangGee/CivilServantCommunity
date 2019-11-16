package com.mdove.civilservantcommunity.ugc.repository

import com.mdove.civilservantcommunity.ugc.bean.UGCTopic
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-15.
 */
class UGCCache {
    var cachePostResp: NormalResp<String>? = null
    var cacheGetTopicsResp: NormalResp<List<UGCTopic>>? = null
}