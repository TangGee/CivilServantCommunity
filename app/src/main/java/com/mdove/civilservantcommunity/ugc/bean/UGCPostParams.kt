package com.mdove.civilservantcommunity.ugc.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-16.
 */
@Parcelize
data class UGCPostParams(@SerializedName("user_info") val userInfo: UserInfo,
                         @SerializedName("typeSingle") val types: List<ArticleType>,
                         @SerializedName("title") val title: String,
                         @SerializedName("content") val content: String,
                         @SerializedName("list_style") val listStyle: String) : Parcelable