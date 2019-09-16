package com.mdove.civilservantcommunity.account.repository

import com.mdove.civilservantcommunity.account.bean.LoginDataResp
import com.mdove.civilservantcommunity.account.bean.MePageDataResp
import com.mdove.civilservantcommunity.account.bean.RegisterDataResp
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-07.
 */
class AccountCache {
    var cacheLoginResp: NormalResp<LoginDataResp>? = null
    var cacheMePageResp: NormalResp<MePageDataResp>? = null
    var cacheRegisterResp: NormalResp<RegisterDataResp>? = null
    var cacheUpdateUserInfoResp: NormalResp<String>? = null
}