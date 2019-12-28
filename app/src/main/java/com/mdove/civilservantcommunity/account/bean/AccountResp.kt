package com.mdove.civilservantcommunity.account.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.UserInfo
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-05.
 */
@Parcelize
data class RegisterDataResp(@SerializedName("user_info") val userInfo: UserInfo? = null) :
    Parcelable

@Parcelize
data class LoginDataResp(@SerializedName("user_info") val userInfo: UserInfo? = null) : Parcelable

@Parcelize
data class MePageDataResp(
    @SerializedName("uid") val uid: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("que_list") val questionList: List<MePageQuestionInfoMePage>? = null,
    @SerializedName("article_list") val articleList: List<MePageArticleInfoMePage>? = null,
    @SerializedName("ansque_list") val answerList: List<MePageAnswerInfoMePage>? = null,
    @SerializedName("user_type") val userType: String
) : Parcelable

@Parcelize
data class UserInfoParams(
    val uid: String,
    val userName: String,
    val phone: String,
    val userType: String
) : Parcelable

fun MePageDataResp.toUserInfoParams(): UserInfoParams {
    return UserInfoParams(this.uid, this.userName, this.phone, this.userType)
}

