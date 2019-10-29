package com.mdove.civilservantcommunity.detailfeed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-09.
 */
@Parcelize
data class DetailFeedResp(
    @SerializedName("aid") val aid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("make_time") val maketime: String? = "",
    @SerializedName("typeSingle") val type: List<ArticleType>? = null,
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable