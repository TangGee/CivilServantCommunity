package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-06.
 */
@Parcelize
data class ArticleResp(
    @SerializedName("aid") val aid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("make_time") val maketime: String? = "",
    @SerializedName("type") val type: List<ArticleType>? = null,
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable

data class FeedPunchResp(val count: Int = 0, var hasPunch: Boolean = false) : BaseFeedResp()
data class FeedUGCResp(val name :String ="打卡"):BaseFeedResp()
data class FeedPlanResp(val name :String ="今日计划"):BaseFeedResp()
data class FeedTodayPlanResp(val name :String ="今日计划"):BaseFeedResp()

@Parcelize
data class FeedArticleResp(val article: ArticleResp):BaseFeedResp(),Parcelable

sealed class BaseFeedResp

