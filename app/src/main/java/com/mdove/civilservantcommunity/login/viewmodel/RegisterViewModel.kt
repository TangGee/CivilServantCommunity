package com.mdove.civilservantcommunity.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.login.RegisterDataResp
import com.mdove.civilservantcommunity.login.RegisterInfoParams
import com.mdove.civilservantcommunity.login.repository.LoginRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by zhaojing on 2019-09-02.
 */
class RegisterViewModel :ViewModel() {
    // u001在职,u002在校生，u003专职备考，u004已上岸，u005宝妈奶爸
    private val identityKeys = mutableListOf("u001","u002","u003","u004","u005")
    var selectIdentity: String? = null
    private val loginRespository =
        LoginRepository()

    private var registerReq = MutableLiveData<RegisterInfoParams>()

    val registerResp:LiveData<Resource<NormalResp<RegisterDataResp>>> = Transformations.switchMap(registerReq){
        loginRespository.login(it)
    }

    fun onClickIdentity(index: Int) {
        selectIdentity = identityKeys[index]
    }

    fun reqRegister(params: RegisterInfoParams){
        registerReq.value = params
    }

}