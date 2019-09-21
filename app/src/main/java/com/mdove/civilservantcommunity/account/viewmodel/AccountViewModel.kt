package com.mdove.civilservantcommunity.account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.account.bean.*
import com.mdove.civilservantcommunity.account.repository.AccountRepository
import com.mdove.civilservantcommunity.account.utils.IdentitysHelper
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-02.
 */
class AccountViewModel : ViewModel() {
    val identitys = IdentitysHelper.identitys
    // u001在职,u002在校生，u003专职备考，u004已上岸，u005宝妈奶爸
    private val identityKeys = mutableListOf("u001", "u002", "u003", "u004", "u005")
    var selectIdentity: String? = null
    private val repository = AccountRepository()
    lateinit var userInfo: UserInfo

    private var registerReq = MutableLiveData<RegisterInfoParams>()
    private var loginReq = MutableLiveData<LoginInfoParams>()
    private var updateUserInfoReq = MutableLiveData<UpdateUserInfoParams>()

    val registerResp: LiveData<Resource<NormalResp<RegisterDataResp>>> =
        Transformations.switchMap(registerReq) {
            repository.register(it)
        }

    val updateUserInfoResp: LiveData<Resource<NormalResp<String>>> =
        Transformations.switchMap(updateUserInfoReq) {
            repository.updateUserInfo(it)
        }

    val loginResp: LiveData<Resource<NormalResp<LoginDataResp>>> =
        Transformations.switchMap(loginReq) {
            repository.login(it)
        }

    fun onClickIdentity(index: Int) {
        selectIdentity = identityKeys[index]
    }

    fun reqRegister(params: RegisterInfoParams) {
        registerReq.value = params
    }

    fun updateUserInfo(name:String) {
        AppConfig.getUserInfo()?.let {
            updateUserInfoReq.value = UpdateUserInfoParams(selectIdentity, it.uid, name)
        }
    }

    fun reqLogin(params: LoginInfoParams) {
        loginReq.value = params
    }
}