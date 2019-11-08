package com.mdove.civilservantcommunity.plan.model

import android.os.Parcelable
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-07.
 */
@Parcelize
data class TimeSchedulePlansParams(
    val data: SinglePlanBean,
    val status: TimeSchedulePlansStatus
) : Parcelable

enum class TimeSchedulePlansStatus {
    SHOW,
    GONE
}

@Parcelize
data class TimeScheduleParams(val data: List<FeedTimeLineFeedTodayPlansResp>) : Parcelable