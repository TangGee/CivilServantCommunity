package com.mdove.civilservantcommunity.plan.repository

import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-10-19.
 */
class PlanCache {
    var mCachePostResp: NormalResp<List<List<SinglePlanBean>>>? = null

    suspend fun getTodayPlans(): NormalResp<List<List<SinglePlanBean>>>? =
        withContext(MDoveBackgroundPool) {
            MainDb.db.todayPlansDao().getTodayPlansRecord()?.resp?.params?.map {
                it.beanSingles.map {
                    it.beanSingle
                }
            }?.takeIf {
                it.isNotEmpty()
            }?.let {
                NormalResp(message = "suc", status = 0, data = it)
            }
        }
}