package com.mdove.civilservantcommunity.feed.repository

import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedCacheModule {
    var mCacheFeedResp: NormalResp<List<ArticleResp>>? = null
}