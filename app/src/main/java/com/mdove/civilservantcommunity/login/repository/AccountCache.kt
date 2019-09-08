package com.mdove.civilservantcommunity.login.repository

import com.mdove.civilservantcommunity.login.bean.RegisterDataResp
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-07.
 */
class AccountCache {
    var cacheLoginResp: NormalResp<String>? = null
    var cacheRegisterResp: NormalResp<RegisterDataResp>? = null
}