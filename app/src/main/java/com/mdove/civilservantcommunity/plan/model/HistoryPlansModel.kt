package com.mdove.civilservantcommunity.plan.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-14.
 */
sealed class HistoryPlansBaseBean

data class HistoryPlansEmptyTipsBean(val name: String = "") : HistoryPlansBaseBean()

@Parcelize
data class HistoryPlansSinglePlanBean(
    @SerializedName("single_plan")
    val beanSingle: SinglePlanBean,
    @SerializedName("single_plan_type")
    var typeSingle: SinglePlanType,
    @SerializedName("single_plan_status")
    val statusSingle: SinglePlanStatus = SinglePlanStatus.NORMAL,
    @SerializedName("time_schedule")
    var timeSchedule: Pair<Long, Long>? = null
) : Parcelable,HistoryPlansBaseBean()