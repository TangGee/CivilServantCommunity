package com.mdove.civilservantcommunity.login.repository

import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.civilservantcommunity.login.bean.LoginDataResp
import com.mdove.civilservantcommunity.login.bean.MePageDataResp
import com.mdove.civilservantcommunity.login.bean.RegisterDataResp
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-07.
 */
class AccountCache {
    var cacheLoginResp: NormalResp<LoginDataResp>? = null
    var cacheMePageResp: NormalResp<MePageDataResp>? = null
    var cacheRegisterResp: NormalResp<RegisterDataResp>? = null
}