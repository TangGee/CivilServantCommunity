package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_DATE
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_DEV
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_QUICK_BTNS
import com.mdove.civilservantcommunity.plan.model.PlanToFeedParams
import com.mdove.civilservantcommunity.plan.model.SinglePlanBeanWrapper
import com.mdove.dependent.common.utils.TimeUtils
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-06.
 */

data class FeedPunchResp(val count: Int = 0, var hasPunch: Boolean = false) : BaseFeedResp(type = -1)

data class FeedDevTitleResp(
    val name: String = "开发者说"
) : BaseFeedResp(type = TYPE_FEED_DEV)

data class FeedDateResp(
    val time: Long,
    val name: String? = null
) : BaseFeedResp(type = TYPE_FEED_DATE) {
    val isSameDay
        get() = TimeUtils.isSameDay(time, System.currentTimeMillis())
}

data class FeedQuickBtnsResp(
    val name: String = "快捷按钮"
) : BaseFeedResp(type = TYPE_FEED_QUICK_BTNS)

data class FeedEncourageTipsResp(
    val name: String = "主Feed鼓励"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_TODAY_PLANS_ENCOURAGE)

data class FeedLoadMoreResp(
    val name: String = "load more"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_LOAD_MORE)

data class FeedQuickEditNewPlanResp(
    val name: String = "快捷发送计划"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_EDIT_NEW_PLAN)

data class FeedTimeLineFeedTitleResp(
    val name: String = "今日推荐"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TITLE)

data class FeedNetworkErrorTitleResp(
    val name: String = "网络错误"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_NETWORK_ERROR)

data class FeedNoContentResp(
    val name: String = "无更多内容"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_NO_CONTENT)


data class FeedBottomPaddingResp(
    val name: String = "底部padding"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_BOTTOM_PADDING)

@Parcelize
data class FeedTimeLineFeedTodayPlansResp(
    val entityId: Long, // 数据库的id
    val date: Long, // 数据库的date
    val sucTime: Long?, // 数据库的date
    val createTime: String, // 数据库的create_date
    val params: SinglePlanBeanWrapper
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN), Parcelable

data class FeedTimeLineFeedTodayPlansTitleResp(
    val name: String = "今日计划的时间轴头部"
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_TITLE)

data class FeedTimeLineFeedTodayPlansTipsTitleResp(
    val name: String = "新一天空计划提示",
    val showApply: Boolean //一键应用之前的的计划
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS)

data class FeedTodayPlanResp(
    val name: String = "今日计划",
    val params: PlanToFeedParams? = null
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_TODAY_PLAN)

data class FeedTodayPlansCheckParams(
    val resp: FeedTimeLineFeedTodayPlansResp,
    val select: Boolean
)

@Parcelize
data class FeedArticleFeedResp(
    val article: MainFeedArticleResp
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_NORMAL_CARD), Parcelable

@Parcelize
data class FeedQuestionFeedResp(
    val question: MainFeedQuestionResp,
    val answer: MainFeedAnswerResp
) : BaseFeedResp(type = MainFeedAdapter.TYPE_FEED_QUESTION_CARD), Parcelable

abstract class BaseFeedResp(var hideEndLine: Boolean = false, val type: Int)

