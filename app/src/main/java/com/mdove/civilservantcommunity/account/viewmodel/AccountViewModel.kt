package com.mdove.civilservantcommunity.account.viewmodel

import android.text.TextUtils
import androidx.lifecycle.*
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
    lateinit var userInfoParamsFromMePage: UserInfoParams

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

    val userInfoParamsLiveData = MediatorLiveData<UserInfoParams>().apply {
        addSource(updateUserInfoResp) {
            if (it.data?.status == 0) {
                value = userInfoParamsFromMePage.copy(
                    userName = updateUserInfoReq.value?.userName
                        ?: userInfoParamsFromMePage.userName,
                    userType = updateUserInfoReq.value?.userType
                        ?: userInfoParamsFromMePage.userType
                )

            }
        }
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

    fun updateUserInfo(name: String) {
        AppConfig.getUserInfo()?.let {
            updateUserInfoReq.value = UpdateUserInfoParams(
                selectIdentity,
                it.uid,
                if (TextUtils.isEmpty(name)) userInfoParamsFromMePage.userName else name
            )
        }
    }

    fun hasChange(name: String): Boolean {
        return !(name == userInfoParamsFromMePage.userName && selectIdentity == userInfoParamsFromMePage.userType)
    }

    fun reqLogin(params: LoginInfoParams) {
        loginReq.value = params
    }
}