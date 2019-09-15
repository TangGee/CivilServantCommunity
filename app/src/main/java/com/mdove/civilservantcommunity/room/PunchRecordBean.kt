package com.mdove.civilservantcommunity.room

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-15.
 */
@Parcelize
data class PunchRecordBean(
    @SerializedName("punch_time")
    var punchTime: Long
) : Parcelable