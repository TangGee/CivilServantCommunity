package com.mdove.civilservantcommunity.feed.repository

import com.mdove.civilservantcommunity.feed.bean.MainFeedResp
import com.mdove.civilservantcommunity.feed.bean.NormalRespWrapper

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedCacheModule {
    var mCacheFeedResp: NormalRespWrapper<List<MainFeedResp>>? = null
}