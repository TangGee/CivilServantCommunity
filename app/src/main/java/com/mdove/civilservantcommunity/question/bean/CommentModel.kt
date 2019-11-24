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

// SendCommentDialogFragment用的
@Parcelize
data class QuestionCommentSendParams(
    val father: QuestionCommentBean? = null,
    val child: QuestionCommentPairBean? = null
) : Parcelable, BaseQuestionCommentBean()

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