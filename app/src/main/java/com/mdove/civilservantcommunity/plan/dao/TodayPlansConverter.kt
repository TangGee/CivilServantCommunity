package com.mdove.civilservantcommunity.plan.dao

import androidx.room.TypeConverter
import com.mdove.dependent.common.gson.GsonProvider

/**
 * Created by MDove on 2019-10-27.
 */
class TodayPlansConverter {
    @TypeConverter
    fun toTodayPlans(value: String): TodayPlansDbBean {
        return GsonProvider.getDefaultGson().fromJson(value, TodayPlansDbBean::class.java)
    }

    @TypeConverter
    fun fromTodayPlans(resp: TodayPlansDbBean): String {
        return GsonProvider.getDefaultGson().toJson(resp)
    }
}