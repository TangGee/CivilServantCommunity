package com.mdove.civilservantcommunity.login.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.login.bean.LoginInfoParams
import com.mdove.civilservantcommunity.login.bean.RegisterDataResp
import com.mdove.civilservantcommunity.login.bean.RegisterInfoParams
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-05.
 */
class AccountRepository {
    private val registerModule = AccountModule()
    private val accountCache = AccountCache()

    fun register(loginInfoParams: RegisterInfoParams): LiveData<Resource<NormalResp<RegisterDataResp>>> {
        return object :
            NetworkBoundResource<NormalResp<RegisterDataResp>, NormalResp<RegisterDataResp>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<RegisterDataResp>) {
                accountCache.cacheRegisterResp = item
            }

            override fun shouldFetch(data: NormalResp<RegisterDataResp>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<RegisterDataResp>> {
                return MutableLiveData<NormalResp<RegisterDataResp>>().apply {
                    value = accountCache.cacheRegisterResp ?: NormalResp<RegisterDataResp>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<RegisterDataResp>>> {
                return registerModule.register(loginInfoParams)
            }
        }.asLiveData()
    }

    fun login(loginInfoParams: LoginInfoParams): LiveData<Resource<NormalResp<String>>> {
        return object :
            NetworkBoundResource<NormalResp<String>, NormalResp<String>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<String>) {
                accountCache.cacheLoginResp = item
            }

            override fun shouldFetch(data: NormalResp<String>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<String>> {
                return MutableLiveData<NormalResp<String>>().apply {
                    value = accountCache.cacheLoginResp ?: NormalResp<String>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<String>>> {
                return registerModule.login(loginInfoParams)
            }
        }.asLiveData()
    }
}