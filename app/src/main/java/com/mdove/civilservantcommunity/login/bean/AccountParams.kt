package com.mdove.civilservantcommunity.login.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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