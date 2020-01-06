package com.mdove.civilservantcommunity.setting.hide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.setting.hide.model.HideRecordBean
import com.mdove.civilservantcommunity.setting.hide.model.TYPE_HIDE_RECORD_NO_CONTENT
import com.mdove.civilservantcommunity.setting.utils.HideRecorder

/**
 * Created by MDove on 2020-01-06.
 */
class HideRecordViewModel : ViewModel() {

    fun observerData(): LiveData<List<HideRecordBean>> {
        val liveData = MutableLiveData<List<HideRecordBean>>()
        HideRecorder.getHideRecordTypes()?.let {
            liveData.postValue(it.map { type ->
                HideRecordBean(type)
            })
        } ?: also {
            liveData.postValue(mutableListOf(HideRecordBean(TYPE_HIDE_RECORD_NO_CONTENT)))
        }
        return liveData
    }
}