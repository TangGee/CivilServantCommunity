package com.mdove.civilservantcommunity.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.dependent.common.network.NormalResp
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-05.
 */
@Parcelize
data class RegisterDataResp(@SerializedName("uid") val uid: String? = "") : Parcelable