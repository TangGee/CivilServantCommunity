package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.NormalUserInfo
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-06.
 */
@Parcelize
data class FeedDataResp(
    @SerializedName("aid") val aid: String? = "",
    @SerializedName("user_info") val userInfo: NormalUserInfo? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val maketime: String? = "",
    @SerializedName("type") val type: String? = "",
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable