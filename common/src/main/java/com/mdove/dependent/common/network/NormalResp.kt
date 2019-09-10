package com.mdove.dependent.common.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019/9/3.
 */
data class NormalResp<T>(
        @SerializedName("msg") val message: String = "fail",
        @SerializedName("data") var data: T? = null,
        @Transient var exception: Exception? = null,
        @SerializedName("status") val status: String? = null
) : Serializable {
    val isSuccess: Boolean
        get() = exception == null && data != null
    val errorCode: String
        get() = when {
            isSuccess -> {
                "success"
            }
            "success" == message && data == null -> {
                "no_data"
            }
            (exception != null) -> {
                exception!!.message ?: "error msg null"
            }
            else -> {
                message
            }
        }
}

@Parcelize
data class NormalErrorResp(@SerializedName("msg") val message: String = "fail",
                           @SerializedName("status") val status: String? = null) : Parcelable