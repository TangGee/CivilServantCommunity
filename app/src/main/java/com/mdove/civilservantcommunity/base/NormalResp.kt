package com.mdove.civilservantcommunity.base

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-06.
 */
@Parcelize
data class NormalUserInfo(
    @SerializedName("uid") val uid: String? = "",
    @SerializedName("username") val username: String? = ""
) : Parcelable