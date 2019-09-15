package com.mdove.civilservantcommunity.login.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
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
    @SerializedName("article_list") val articleList: List<ArticleResp>? = null,
    @SerializedName("user_type") val userType: String
) : Parcelable