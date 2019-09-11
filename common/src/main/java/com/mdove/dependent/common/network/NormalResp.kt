package com.mdove.dependent.common.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by MDove on 2019/9/3.
 */
data class NormalResp<T>(
        @SerializedName("msg") val message: String = "fail",
        @SerializedName("data") var data: T? = null,
        @Transient var exception: Exception? = null,
        @SerializedName("status") val status: Int? = null
) : Serializable

@Parcelize
data class NormalErrorResp(@SerializedName("msg") val message: String = "fail",
                           @SerializedName("status") val status: Int? = null) : Parcelable