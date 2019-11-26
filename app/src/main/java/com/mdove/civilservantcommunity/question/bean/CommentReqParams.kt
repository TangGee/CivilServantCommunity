package com.mdove.civilservantcommunity.question.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.UserInfo
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-21.
 */
// 一级评论
@Parcelize
data class CommentReqParams(
    @SerializedName("anid") val anid: String,
    @SerializedName("comment_info") val info: CommentInfo,
    @SerializedName("content") val content: String,
    @SerializedName("is_father") val isFather: String = "1"
) : Parcelable

// 二级评论
@Parcelize
data class CommentToReqParams(
    @SerializedName("anid") val anid: String,
    @SerializedName("father_id") val fatherId: String,
    @SerializedName("comment_info") val info: CommentInfo,
    @SerializedName("to_info") val toInfo: ToInfo,
    @SerializedName("content") val content: String,
    @SerializedName("is_father") val isFather: String = "2"
) : Parcelable

@Parcelize
data class ToInfo(
    @SerializedName("to_id") val toId: String,
    @SerializedName("to_uid") val toUid: String,
    @SerializedName("to_username") val toUsername: String
) : Parcelable

@Parcelize
data class AnswerReqParams(
    @SerializedName("user_info") val userInfo: UserInfo,
    @SerializedName("qid") val qid: String,
    @SerializedName("content") val content: String,
    @SerializedName("list_style") val listStyle: String
) : Parcelable

@Parcelize
data class CommentInfo(
    @SerializedName("uid") val uid: String,
    @SerializedName("user_name") val userName: String
) : Parcelable