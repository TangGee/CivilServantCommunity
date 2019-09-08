package com.mdove.civilservantcommunity.login.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-08.
 */
@Parcelize
data class UserInfo(@SerializedName("uid") val uid: String) : Parcelable