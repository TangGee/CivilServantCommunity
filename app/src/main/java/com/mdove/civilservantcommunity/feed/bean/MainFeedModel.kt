package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.ugc.bean.UGCTopic
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-17.
 *
 * Feed最大的数据结构
 */
@Parcelize
data class MainFeedResp(
    @SerializedName("feed_style") val feedStyle: String? = "",
    @SerializedName("article") val article: MainFeedArticleResp? = null,
    @SerializedName("answer") val answer: MainFeedAnswerResp? = null,
    @SerializedName("question") val question: MainFeedQuestionResp? = null
) : Parcelable

@Parcelize
data class MainFeedArticleResp(
    @SerializedName("aid") val aid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: String? = "",
    @SerializedName("typeSingle") val type: List<ArticleType>? = null,
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable

@Parcelize
data class MainFeedQuestionResp(
    @SerializedName("qid") val qid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("type") val topics: UGCTopic? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: String? = "",
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable

@Parcelize
data class MainFeedAnswerResp(
    @SerializedName("anid") val anid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("qid") val qid: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: String? = "",
    @SerializedName("list_style") val listStyle: String? = ""
) : Parcelable

fun MainFeedResp.toMainFeedResp(): BaseFeedResp? {
    return when (this.feedStyle) {
        "1" -> {
            return if (this.article != null) {
                FeedArticleFeedResp(this.article)
            } else null
        }
        "3" -> {
            if (this.question != null && this.answer != null) {
                FeedQuestionFeedResp(this.question, this.answer)
            } else null
        }
        else -> null
    }
}

const val QUESTION_QUICK_SEND_LIST_STYLE = "4"