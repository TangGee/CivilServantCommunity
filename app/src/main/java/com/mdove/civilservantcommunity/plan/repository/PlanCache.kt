package com.mdove.civilservantcommunity.plan.repository

import com.mdove.civilservantcommunity.plan.SinglePlanBeanWrapper
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.utils.TimeUtils
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-10-19.
 */
class PlanCache {
    var mCachePostResp: NormalResp<List<List<SinglePlanBeanWrapper>>>? = null

    suspend fun getTodayPlans(): NormalResp<List<List<SinglePlanBeanWrapper>>>? =
        withContext(MDoveBackgroundPool) {
            MainDb.db.todayPlansDao().getTodayPlansRecord(TimeUtils.getDateFromSQL())?.resp?.params?.map {
                it.beanSingles
            }?.takeIf {
                it.isNotEmpty()
            }?.let {
                NormalResp(message = "suc", status = 0, data = it)
            }
        }
}