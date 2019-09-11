package com.mdove.civilservantcommunity.base.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-08.
 */
@Parcelize
data class UserInfo(
    @SerializedName("uid") val uid: String,
    @SerializedName("user_name") val username: String
) : Parcelable

@Parcelize
data class ArticleType(
    @SerializedName("type_name") val typeName: String,
    @SerializedName("type_id") val typeId: String
) : Parcelable