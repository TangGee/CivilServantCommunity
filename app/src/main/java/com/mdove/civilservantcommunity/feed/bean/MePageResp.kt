package com.mdove.civilservantcommunity.feed.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.question.bean.AnswerDetailAnBean
import com.mdove.civilservantcommunity.question.bean.AnswerDetailBean
import com.mdove.civilservantcommunity.question.bean.QuestionDetailBean
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-09.
 */
@Parcelize
data class DetailFeedResp(
    @SerializedName("que_list") val questionList: List<DetailQuestionInfo>? = null,
    @SerializedName("article_list") val articleList: List<DetailArticleInfo>? = null,
    @SerializedName("ansque_list") val answerList: List<DetailAnswerInfo>? = null
) : Parcelable

@Parcelize
data class DetailArticleInfo(
    @SerializedName("aid") val aid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("make_time") val maketime: Long? = null,
    @SerializedName("typeSingle") val type: List<ArticleType>? = null,
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable

@Parcelize
data class DetailQuestionInfo(
    @SerializedName("feed_style") val feedStyle: String? = "",
    @SerializedName("question") val question: QuestionDetailBean? = null
) : Parcelable

@Parcelize
data class DetailAnswerInfo(
    @SerializedName("feed_style") val feedStyle: String? = "",
    @SerializedName("question") val question: QuestionDetailBean? = null,
    @SerializedName("answer") val answer: AnswerDetailAnBean? = null
) : Parcelable