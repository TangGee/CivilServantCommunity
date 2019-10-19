package com.mdove.civilservantcommunity.plan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlanModuleModel(val data: List<PlanModuleBean>? = null):Parcelable

@Parcelize
data class PlanModuleBean(@SerializedName("uid") val uid: String? = null,
                          @SerializedName("module_id")val  moduleId: String? = null,
                          @SerializedName("module_name")val  moduleName: String? = null,
                          @SerializedName("part")val  part: String? = null,
                          @SerializedName("list_style")val  listStyle: String? = null,
                          @SerializedName("content")val  content: String? = null,
                          @SerializedName("factor")val  factor: PlanFactorModel? = null) : Parcelable

@Parcelize
data class PlanFactorModel(@SerializedName("factor_time") val factorTime: String? = null,
                           @SerializedName("factor_count") val factorCount: String? = null,
                           @SerializedName("factor_count_all") val factorCountAll: String? = null,
                           @SerializedName("factor_other")val  factorOther: String? = null):Parcelable
