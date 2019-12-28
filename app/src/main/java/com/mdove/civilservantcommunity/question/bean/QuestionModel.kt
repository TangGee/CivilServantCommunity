package com.mdove.civilservantcommunity.question.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.ugc.bean.UGCTopic
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-24.
 */
@Parcelize
data class QuestionReqParams(val qid: String) : Parcelable

data class QuestionDetailResp(
    @SerializedName("question") val question: QuestionDetailBean? = null,
    @SerializedName("answer") val answers: List<AnswerDetailBean>? = null
)

sealed class BaseDetailQuestionBean

data class DetailQuestionErrorIconBean(val name: String = "error") :BaseDetailQuestionBean()
data class DetailQuestionAnswerTitleBean(val name: String = "error") :BaseDetailQuestionBean()

@Parcelize
data class QuestionDetailBean(
    @SerializedName("qid") val qid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("type") val topics: UGCTopic? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: Long? = null,
    @SerializedName("list_style") val listStyle: String? = null
) : Parcelable,BaseDetailQuestionBean()

@Parcelize
data class AnswerDetailBean(
    @SerializedName("an") val an: AnswerDetailAnBean? = null,
    @SerializedName("play_comment_onelist") val playCommentOnelist: List<PlayCommentOnelisBean>? = null,
    @SerializedName("play_comment_twolist") val playCommentTwolistplayCommentOnelist: List<PlayCommentTwolisBean>? = null
) : Parcelable, BaseDetailQuestionBean()

@Parcelize
data class QuestionDetailErrorBean(
    val an: String = "error"
) : Parcelable, BaseDetailQuestionBean()

@Parcelize
data class AnswerDetailAnBean(
    @SerializedName("anid") val anid: String? = "",
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("qid") val qid: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: Long? = null,
    @SerializedName("list_style") val listStyle: String? = ""
) : Parcelable

@Parcelize
data class PlayCommentOnelisBean(
    @SerializedName("anid") val anid: String? = "",
    @SerializedName("comment_info") val info: CommentInfoBean? = null,
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: Long? = null,
    @SerializedName("list_style") val listStyle: String? = ""
) : Parcelable

@Parcelize
data class PlayCommentTwolisBean(
    @SerializedName("anid") val anid: String? = "",
    @SerializedName("comment_info") val commentInfo: CommentInfoBean? = null,
    @SerializedName("to_info") val toInfo: ToInfoBean? = null,
    @SerializedName("content") val content: String? = "",
    @SerializedName("father_id") val fatherId: String? = "",
    @SerializedName("maketime") val makeTime: Long? = null,
    @SerializedName("list_style") val listStyle: String? = ""
) : Parcelable

@Parcelize
data class CommentInfoBean(
    @SerializedName("uid") val uid: String? = "",
    @SerializedName("user_name") val userName: String? = "",
    @SerializedName("comment_id") val commentId: String? = ""
) : Parcelable

@Parcelize
data class ToInfoBean(
    @SerializedName("to_id") val toId: String? = "",
    @SerializedName("to_username") val toUserName: String? = ""
) : Parcelable

