package com.mdove.civilservantcommunity.roles

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

/**
 * Created by MDove on 2020-01-01.
 */
class SelecctRolesViewModel : ViewModel() {
    val data = MutableLiveData<List<SelectRolesBean>>().apply {
        value = mutableListOf(
            SelectRolesBean("1", "公务员", true),
            SelectRolesBean("2", "程序员", true),
            SelectRolesBean("3", "经济学"),
            SelectRolesBean("4", "漫画"),
            SelectRolesBean("5", "篮球"),
            SelectRolesBean("6", "四六级"),
            SelectRolesBean("7", "考研")
        )
    }
}

@Parcelize
data class SelectRolesBean(val id: String, val title: String, val isSys: Boolean = false) : Parcelable

@Parcelize
data class SelectRolesResult(val params: SelectRolesBean?, val status: Status) : Parcelable

enum class Status {
    CANCEL,
    ERROR,
    SUC
}