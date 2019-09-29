package com.mdove.civilservantcommunity.account

import android.os.Parcelable
import com.mdove.civilservantcommunity.account.bean.UserInfoParams
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2019-09-29.
 */
@Parcelize
data class UpdateInfoResult(val params: UserInfoParams?, val status: Status) : Parcelable

enum class Status{
    CANCEL,
    ERROR,
    SUC
}