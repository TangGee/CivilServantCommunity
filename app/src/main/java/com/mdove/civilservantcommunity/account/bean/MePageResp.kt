package com.mdove.civilservantcommunity.account.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.question.bean.AnswerDetailAnBean
import com.mdove.civilservantcommunity.question.bean.QuestionDetailBean
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by MDove on 2019-09-09.
 */
@Parcelize
data class MePageArticleInfoMePage(
    @SerializedName("aid") val aid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("make_time") val maketime: String? = null,
    @SerializedName("typeSingle") val type: List<ArticleType>? = null,
    @SerializedName("list_style") val listStyle: Int? = 0
) : Parcelable, BaseMePageDetailInfo()

@Parcelize
data class MePageQuestionInfoMePage(
    @SerializedName("feed_style") val feedStyle: String? = "",
    @SerializedName("question") val question: QuestionDetailBean? = null
) : Parcelable, BaseMePageDetailInfo()

@Parcelize
data class MePageAnswerInfoMePage(
    @SerializedName("feed_style") val feedStyle: String? = "",
    @SerializedName("question") val question: QuestionDetailBean? = null,
    @SerializedName("answer") val answer: AnswerDetailAnBean? = null
) : Parcelable, BaseMePageDetailInfo()

sealed class BaseMePageDetailInfo :Serializable

data class MePageArticleErrorIconInfo(val name: String = "") : BaseMePageDetailInfo()
data class MePageArticleErrorTitleInfo(val name: String = "") : BaseMePageDetailInfo()
data class MePageArticleAddArticleInfo(val name: String = "") : BaseMePageDetailInfo()