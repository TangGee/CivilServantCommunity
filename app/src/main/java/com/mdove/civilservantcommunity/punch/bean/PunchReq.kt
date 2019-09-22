package com.mdove.civilservantcommunity.punch.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.room.PunchRecordBean
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-15.
 */
@Parcelize
data class PunchReq(@SerializedName("uid") val uid: String,
                    @SerializedName("call_time") val callTime: Long) : Parcelable

fun PunchReq.toPunchRecordBean(): PunchRecordBean{
    return PunchRecordBean(this.callTime)
}
