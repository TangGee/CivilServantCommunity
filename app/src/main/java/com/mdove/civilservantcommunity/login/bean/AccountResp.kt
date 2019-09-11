package com.mdove.civilservantcommunity.login.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.dependent.common.network.NormalResp
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-05.
 */
@Parcelize
data class RegisterDataResp(@SerializedName("user_info") val userInfo: UserInfo? = null) : Parcelable

@Parcelize
data class LoginDataResp(@SerializedName("user_info") val userInfo: UserInfo? = null) : Parcelable