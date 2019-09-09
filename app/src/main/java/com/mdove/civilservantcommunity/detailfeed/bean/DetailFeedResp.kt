package com.mdove.civilservantcommunity.detailfeed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-09.
 */
@Parcelize
data class DetailFeedResp(
    @SerializedName("aid") val aid: String? = "",
//    @SerializedName("user_info") val userInfo: NormalUserInfo? = null,
    @SerializedName("username") val userName: String? = "",
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val maketime: String? = "",
    @SerializedName("type") val type: String? = "",
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable