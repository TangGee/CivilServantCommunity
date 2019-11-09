package com.mdove.civilservantcommunity.plan.model

import android.os.Parcelable
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-07.
 */
@Parcelize
data class TimeSchedulePlansParams(
    val data: SinglePlanBean,
    val status: TimeSchedulePlansStatus,
    var timeSchedule: Pair<Long, Long>? = null
) : Parcelable

enum class TimeSchedulePlansStatus {
    SHOW,
    GONE
}

@Parcelize
data class TimeScheduleParams(val data: List<TimeSchedulePlansParams>) : Parcelable

@Parcelize
data class TimeScheduleToFeedResult(
    val data: TimeScheduleParams?,
    val timeScheduleStatus: TimeScheduleStatus
) : Parcelable

enum class TimeScheduleStatus {
    CANCEL,
    ERROR,
    SUC
}