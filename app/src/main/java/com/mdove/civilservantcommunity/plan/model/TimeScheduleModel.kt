package com.mdove.civilservantcommunity.plan.model

import android.os.Parcelable
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-07.
 */
@Parcelize
data class TimeSchedulePlansParams(
    val title: String
) : Parcelable

@Parcelize
data class TimeScheduleParams(val data: List<FeedTimeLineFeedTodayPlansResp>) : Parcelable