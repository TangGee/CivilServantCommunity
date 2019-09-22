package com.mdove.civilservantcommunity.punch

import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.utils.TimeUtils
import kotlinx.coroutines.withContext

object PunchHelper {
    suspend fun hasPunchToday(): Boolean {
        return withContext(MDoveBackgroundPool) {
            MainDb.db.punchRecordDao().getLastPunch()?.let {
                TimeUtils.isSameDay(it.record.punchTime)
            } ?: false
        }
    }
}