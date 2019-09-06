package com.mdove.civilservantcommunity.login.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.login.RegisterDataResp
import com.mdove.civilservantcommunity.login.RegisterInfoParams
import com.mdove.dependent.common.network.AppExecutorsImpl
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.NetworkBoundResource
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-05.
 */
class LoginRepository {
    private val registerModule = RegisterModule()
    fun login(loginInfoParams: RegisterInfoParams): LiveData<Resource<NormalResp<RegisterDataResp>>> {
        return object :
            NetworkBoundResource<NormalResp<RegisterDataResp>, NormalResp<RegisterDataResp>>(
                AppExecutorsImpl()
            ) {
            override fun saveCallResult(item: NormalResp<RegisterDataResp>) {
                // 暂不需要
            }

            override fun shouldFetch(data: NormalResp<RegisterDataResp>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<NormalResp<RegisterDataResp>> {
                // 暂不需要
                return MutableLiveData<NormalResp<RegisterDataResp>>().apply {
                    value = NormalResp<RegisterDataResp>()
                }
            }

            override fun createCall(): LiveData<ApiResponse<NormalResp<RegisterDataResp>>> {
                return registerModule.login(loginInfoParams)
            }
        }.asLiveData()
    }
}