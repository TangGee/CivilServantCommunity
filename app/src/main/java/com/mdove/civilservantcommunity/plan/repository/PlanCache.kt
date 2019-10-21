package com.mdove.civilservantcommunity.plan.repository

import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-10-19.
 */
class PlanCache {
    var cachePostResp: NormalResp<List<List<PlanModuleBean>>>? = null
}