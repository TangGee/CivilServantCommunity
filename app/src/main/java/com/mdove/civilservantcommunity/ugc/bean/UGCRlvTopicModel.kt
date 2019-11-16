package com.mdove.civilservantcommunity.ugc.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-11-02.
 */
@Parcelize
data class UGCRlvTopicBean(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("select_status") val selectStatus: Boolean
) : Parcelable