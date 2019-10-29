package com.mdove.civilservantcommunity.plan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SinglePlanBean(
    @SerializedName("uid") val uid: String? = null,
    @SerializedName("module_id") val moduleId: String? = null,
    @SerializedName("module_name") val moduleName: String? = null,
    @SerializedName("part") val part: String? = null,
    @SerializedName("list_style") val listStyle: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("factor") val factor: PlanFactorModel? = null
) : Parcelable

@Parcelize
data class SinglePlanBeanWrapper(
    val beanSingle: SinglePlanBean,
    val typeSingle: SinglePlanType
) : Parcelable

enum class SinglePlanType{
    SYS_PLAN,
    CUSTOM_PLAN
}

@Parcelize
data class PlanModuleBean(
    val beanSingles: List<SinglePlanBeanWrapper>,
    val moduleType: PlanModuleType
) : Parcelable

enum class PlanModuleType{
    PADDING,
    NORMAL,
    BTN_OK
}


@Parcelize
data class PlanFactorModel(
    @SerializedName("factor_time") val factorTime: String? = null,
    @SerializedName("factor_count") val factorCount: String? = null,
    @SerializedName("factor_count_all") val factorCountAll: String? = null,
    @SerializedName("factor_other") val factorOther: String? = null
) : Parcelable

@Parcelize
data class PlanToFeedParams(val data: List<SinglePlanBean>) : Parcelable

@Parcelize
data class FeedTodayPlanParams(val title: String) : Parcelable

@Parcelize
data class PlanToFeedResult(val params: PlanToFeedParams?, val status: Status) : Parcelable

enum class Status {
    CANCEL,
    ERROR,
    SUC
}