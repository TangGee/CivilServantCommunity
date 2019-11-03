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
    @SerializedName("single_plan")
    val beanSingle: SinglePlanBean,
    @SerializedName("single_plan_type")
    var typeSingle: SinglePlanType,
    @SerializedName("single_plan_status")
    val statusSingle: SinglePlanStatus = SinglePlanStatus.NORMAL
) : Parcelable

@Parcelize
enum class SinglePlanStatus : Parcelable {
    SELECT,
    DELETE,
    NORMAL,
}

@Parcelize
enum class SinglePlanType : Parcelable {
    SYS_PLAN,
    LAST_PLAN,
    CUSTOM_PLAN,
    CUSTOM_PLAN_BTN
}

@Parcelize
data class PlanModuleBean(
    val moduleId: String,
    val moduleName: String,
    val beanSingles: List<SinglePlanBeanWrapper>,
    val moduleType: PlanModuleType,
    val moduleStatus: PlanModuleStatus,
    val changeVersion: Int = 0 // 对于module来说，它的子item发生变化 version++。这个Diff时就有变化了
) : Parcelable

enum class PlanModuleType {
    PADDING,
    NORMAL,
    BTN_OK
}

enum class PlanModuleStatus {
    NORMAL,
    DELETE
}

@Parcelize
data class PlanFactorModel(
    @SerializedName("factor_time") val factorTime: String? = null,
    @SerializedName("factor_count") val factorCount: String? = null,
    @SerializedName("factor_count_all") val factorCountAll: String? = null,
    @SerializedName("factor_other") val factorOther: String? = null
) : Parcelable

@Parcelize
data class PlanToFeedParams(
    val entityId: Long,
    val insertDate: Long,
    val createDate: String,
    val sucDate: Long? = null,
    val data: List<PlanModuleBean>
) : Parcelable

@Parcelize
data class PlanToFeedResult(val params: PlanToFeedParams?, val status: Status) : Parcelable

enum class Status {
    CANCEL,
    ERROR,
    SUC
}