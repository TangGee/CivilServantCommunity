package com.mdove.civilservantcommunity.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.login.bean.LoginInfoParams
import com.mdove.civilservantcommunity.login.bean.RegisterDataResp
import com.mdove.civilservantcommunity.login.bean.RegisterInfoParams
import com.mdove.civilservantcommunity.login.repository.AccountRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by zhaojing on 2019-09-02.
 */
class AccountViewModel : ViewModel() {
    val identitys = arrayListOf("在校生", "在职人员", "专职备考", "宝妈奶爸", "已上岸")
    // u001在职,u002在校生，u003专职备考，u004已上岸，u005宝妈奶爸
    private val identityKeys = mutableListOf("u001", "u002", "u003", "u004", "u005")
    var selectIdentity: String? = null
    private val repository =
        AccountRepository()

    private var registerReq = MutableLiveData<RegisterInfoParams>()
    private var loginReq = MutableLiveData<LoginInfoParams>()

    val registerResp: LiveData<Resource<NormalResp<RegisterDataResp>>> =
        Transformations.switchMap(registerReq) {
            repository.register(it)
        }

    val loginResp: LiveData<Resource<NormalResp<String>>> = Transformations.switchMap(loginReq) {
        repository.login(it)
    }

    fun onClickIdentity(index: Int) {
        selectIdentity = identityKeys[index]
    }

    fun reqRegister(params: RegisterInfoParams) {
        registerReq.value = params
    }

    fun reqLogin(params: LoginInfoParams) {
        loginReq.value = params
    }
}