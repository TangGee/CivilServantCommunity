package com.mdove.civilservantcommunity.account.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.account.UpdateInfoResult
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-06.
 */
@Parcelize
data class RegisterInfoParams(
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("user_type") val userType: String
) : Parcelable

@Parcelize
data class LoginInfoParams(
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String
) : Parcelable

@Parcelize
data class UpdateUserInfoParams(
    @SerializedName("user_type") val userType: String? = null,
    @SerializedName("uid") val uid: String? = null,
    @SerializedName("user_name") val userName: String? = null,
    @SerializedName("password") val password: String? = null
) : Parcelable