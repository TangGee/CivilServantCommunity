package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.plan.model.PlanToFeedParams
import com.mdove.civilservantcommunity.plan.model.SinglePlanBeanWrapper
import com.mdove.dependent.common.utils.TimeUtils
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
data class FeedDevTitleResp(val name: String = "开发者说") : BaseFeedResp()

data class FeedPlanResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedDateResp(
    val time: Long
) : BaseFeedResp() {
    val isSameDay
        get() = TimeUtils.isSameDay(time, System.currentTimeMillis())
}

data class FeedQuickBtnsResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedQuickEditNewPlanResp(
    val name: String = "快捷发送计划"
) : BaseFeedResp()

data class FeedTimeLineFeedTitleResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedNetworkErrorTitleResp(
    val name: String = "今日计划"
) : BaseFeedResp()

data class FeedPaddingStub(
    val name: String = "今日计划"
) : BaseFeedResp()


@Parcelize
data class FeedTimeLineFeedTodayPlansResp(
    val entityId: Long, // 数据库的id
    val date: Long, // 数据库的date
    val sucTime: Long?, // 数据库的date
    val createTime: String, // 数据库的create_date
    val params: SinglePlanBeanWrapper
) : BaseFeedResp(), Parcelable

data class FeedTimeLineFeedTodayPlansTitleResp(
    val name: String = "今日计划的时间轴头部"
) : BaseFeedResp()

data class FeedTimeLineFeedTodayPlansTipsTitleResp(
    val name: String = "新一天空计划提示"
) : BaseFeedResp()

data class FeedTodayPlanResp(
    val name: String = "今日计划",
    val params: PlanToFeedParams? = null
) : BaseFeedResp()

data class FeedTodayPlansCheckParams(
    val resp: FeedTimeLineFeedTodayPlansResp,
    val select: Boolean
)

@Parcelize
data class FeedArticleResp(
    val article: ArticleResp,
    var hideEndLine: Boolean = false
) : BaseFeedResp(), Parcelable

sealed class BaseFeedResp

