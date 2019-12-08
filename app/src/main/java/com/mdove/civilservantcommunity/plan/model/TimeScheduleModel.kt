package com.mdove.civilservantcommunity.plan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-07.
 */
@Parcelize
data class TimeSchedulePlansParams(
    val data: SinglePlanBean,
    override var status: TimeSchedulePlansStatus = TimeSchedulePlansStatus.SHOW,
    var timeSchedule: Pair<Long, Long>? = null // first:8 second:9 8:00 - 9:00
) : Parcelable, TimeScheduleBaseParams(status)


enum class TimeSchedulePlansStatus {
    SHOW,
    GONE
}

open class TimeScheduleBaseParams(open var status: TimeSchedulePlansStatus = TimeSchedulePlansStatus.SHOW)

// 初始没有任何计划
data class TimeScheduleEmptyParams(override var status: TimeSchedulePlansStatus = TimeSchedulePlansStatus.SHOW) :
    TimeScheduleBaseParams(status)

//  计划已经都被拖入time块
data class TimeScheduleNoPlanParams(override var status: TimeSchedulePlansStatus = TimeSchedulePlansStatus.SHOW) :
    TimeScheduleBaseParams(status)

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