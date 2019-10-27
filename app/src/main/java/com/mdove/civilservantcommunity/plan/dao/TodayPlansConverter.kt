package com.mdove.civilservantcommunity.plan.dao

import androidx.room.TypeConverter
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.dependent.common.gson.GsonProvider

/**
 * Created by MDove on 2019-10-27.
 */
class TodayPlansConverter {
    @TypeConverter
    fun toTodayPlans(value: String): FeedTimeLineFeedTodayPlansResp {
        return GsonProvider.getDefaultGson().fromJson(value, FeedTimeLineFeedTodayPlansResp::class.java)
    }

    @TypeConverter
    fun fromTodayPlans(resp: FeedTimeLineFeedTodayPlansResp): String {
        return GsonProvider.getDefaultGson().toJson(resp)
    }
}