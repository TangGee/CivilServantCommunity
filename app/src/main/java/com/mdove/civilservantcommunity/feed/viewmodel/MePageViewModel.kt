package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.account.bean.MePageDataResp
import com.mdove.civilservantcommunity.account.repository.AccountRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-11.
 */
class MePageViewModel : ViewModel() {
    private val accountRepository = AccountRepository()

    private val loadType = MutableLiveData<String>()
    val data: LiveData<Resource<NormalResp<MePageDataResp>>> = Transformations.switchMap(loadType) {
        accountRepository.mePage(it)
    }

    fun reqMePage(uid: String) {
        loadType.value = uid
    }
}