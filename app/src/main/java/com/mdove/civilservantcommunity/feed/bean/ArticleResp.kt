package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.plan.PlanToFeedParams
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
    @SerializedName("typeSingle") val type: List<ArticleType>? = null,
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable

data class FeedPunchResp(val count: Int = 0, var hasPunch: Boolean = false) : BaseFeedResp()
data class FeedUGCResp(val name: String = "打卡") : BaseFeedResp()

data class FeedPlanResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedDateResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedQuickBtnsResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedTimeLineFeedTitleResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedPaddingStub(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedTimeLineFeedTodayPlansRespWrapper(
    val entityId: Long, // 数据库的id
    val date: Long, // 数据库的date
    val sucTime: Long?, // 数据库的date
    val createTime: String, // 数据库的create_date
    val resp: FeedTimeLineFeedTodayPlansResp
) : BaseFeedResp()

@Parcelize
data class FeedTimeLineFeedTodayPlansResp(
    @SerializedName("params")
    val params: SinglePlanBean,
    val select: Boolean = false
) : BaseFeedResp(), Parcelable

data class FeedTimeLineFeedTodayPlansTitleResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedTodayPlanResp(
    val name: String = "今日计划",
    val params: PlanToFeedParams? = null
) : BaseFeedResp()

data class FeedTodayPlansCheckParams(
    val wrapper: FeedTimeLineFeedTodayPlansRespWrapper,
    val select: Boolean
)

@Parcelize
data class FeedArticleResp(
    val article: ArticleResp,
    var hideEndLine: Boolean = false
) :
    BaseFeedResp(), Parcelable

sealed class BaseFeedResp

