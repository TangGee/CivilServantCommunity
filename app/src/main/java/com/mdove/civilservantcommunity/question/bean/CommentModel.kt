package com.mdove.civilservantcommunity.question.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by MDove on 2019-11-24.
 */
@Parcelize
data class QuestionCommentPairBean(
    val father: QuestionCommentBean,
    val child: QuestionCommentChildBean
) : Parcelable, BaseQuestionCommentBean()

// 一级评论的Params
@Parcelize
data class OneCommentSendParams(
    val commentInfo: CommentInfoBean? = null,
    val isFather: String = "1",
    override val content: String?,
    val anid: String?,
    override val listStyle: String?
) : Parcelable, BaseCommentSendParams

// 二级评论的Params
@Parcelize
data class TwoCommentSendParams(
    val commentInfo: CommentInfoBean? = null,
    val isFather: String = "2",
    val fatherId: String? = null,
    override val content: String?,
    val anid: String?,
    override val listStyle: String?
) : Parcelable, BaseCommentSendParams

// 回答某问题的Params
@Parcelize
data class AnswerCommentSendParams(
    val qid :String?,
    val questionUserName :String?,
    override val content: String?,
    override val listStyle: String?
) : Parcelable, BaseCommentSendParams

interface BaseCommentSendParams : Parcelable {
    val content: String?
    val listStyle: String?
}

@Parcelize
data class QuestionCommentBean(
    @SerializedName("anid") val anid: String? = "",
    @SerializedName("comment_info") val info: CommentInfoBean? = null,
    @SerializedName("content") val content: String? = "",
    @SerializedName("maketime") val makeTime: Long? = null,
    @SerializedName("list_style") val listStyle: String? = ""
) : Parcelable, BaseQuestionCommentBean()

@Parcelize
data class QuestionCommentChildBean(
    @SerializedName("anid") val anid: String? = "",
    @SerializedName("comment_info") val commentInfo: CommentInfoBean? = null,
    @SerializedName("to_info") val toInfo: ToInfoBean? = null,
    @SerializedName("content") val content: String? = "",
    @SerializedName("father_id") val fatherId: String? = "",
    @SerializedName("maketime") val makeTime: Long? = null,
    @SerializedName("list_style") val listStyle: String? = ""
) : Parcelable, BaseQuestionCommentBean()

sealed class BaseQuestionCommentBean : Serializable

fun PlayCommentOnelisBean.toQuestionCommentBean(): QuestionCommentBean {
    return QuestionCommentBean(this.anid, this.info, this.content, this.makeTime, this.listStyle)
}

fun PlayCommentTwolisBean.toQuestionCommentChildBean(): QuestionCommentChildBean {
    return QuestionCommentChildBean(
        this.anid,
        this.commentInfo,
        this.toInfo,
        this.content,
        this.fatherId,
        this.makeTime,
        this.listStyle
    )
}