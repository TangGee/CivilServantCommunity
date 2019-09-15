package com.mdove.civilservantcommunity.room

import androidx.room.TypeConverter
import com.mdove.dependent.common.gson.GsonProvider

/**
 * Created by MDove on 2019-09-15.
 */
class PunchRecordConverter {
    @TypeConverter
    fun toPunchRecordBean(value: String): PunchRecordBean {
        return GsonProvider.getDefaultGson().fromJson(value, PunchRecordBean::class.java)
    }

    @TypeConverter
    fun fromPunchRecordBean(state: PunchRecordBean): String {
        return GsonProvider.getDefaultGson().toJson(state)
    }
}