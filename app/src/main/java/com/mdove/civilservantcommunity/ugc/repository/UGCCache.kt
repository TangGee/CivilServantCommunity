package com.mdove.civilservantcommunity.ugc.repository

import com.mdove.civilservantcommunity.login.bean.LoginDataResp
import com.mdove.civilservantcommunity.login.bean.MePageDataResp
import com.mdove.civilservantcommunity.login.bean.RegisterDataResp
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-15.
 */
class UGCCache {
    var cacheLoginResp: NormalResp<LoginDataResp>? = null
    var cacheMePageResp: NormalResp<MePageDataResp>? = null
    var cacheRegisterResp: NormalResp<RegisterDataResp>? = null
}