package com.mdove.civilservantcommunity.punch.repository

import com.mdove.civilservantcommunity.punch.bean.PunchParams
import com.mdove.civilservantcommunity.punch.bean.toPunchRecordBean
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.civilservantcommunity.room.PunchRecordEntity
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-09-15.
 */
class PunchCache {
    var cacheResp: NormalResp<String>? = null

    fun setCache(params: PunchParams, resp: NormalResp<String>) {
        MainDb.db.punchRecordDao().insert(PunchRecordEntity(record = params.toPunchRecordBean()))
        cacheResp = resp
    }
}