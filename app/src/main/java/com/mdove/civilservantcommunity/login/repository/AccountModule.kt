package com.mdove.civilservantcommunity.login.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.login.bean.LoginDataResp
import com.mdove.civilservantcommunity.login.bean.LoginInfoParams
import com.mdove.civilservantcommunity.login.bean.RegisterDataResp
import com.mdove.civilservantcommunity.login.bean.RegisterInfoParams
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.apiservice.AppDependsProvider
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.api.ApiErrorResponse
import com.mdove.dependent.common.networkenhance.api.ApiResponse
import com.mdove.dependent.common.networkenhance.api.ApiSuccessResponse
import com.mdove.dependent.common.utils.fromServerResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by MDove on 2019-09-06.
 */
class AccountModule {
    fun register(params: RegisterInfoParams): LiveData<ApiResponse<NormalResp<RegisterDataResp>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<RegisterDataResp>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/user/create").buildUpon()
        builder.appendQueryParameter("phone", params.phone)
        builder.appendQueryParameter("password", params.password)
        builder.appendQueryParameter("usertype", params.userType)
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<RegisterDataResp> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<RegisterDataResp>(exception = e)
            }
            if (resp.exception == null) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(
                    ApiErrorResponse(
                        resp.exception ?: RuntimeException("unknown_error")
                    )
                )
            }
        }
        return liveData
    }

    fun login(params: LoginInfoParams): LiveData<ApiResponse<NormalResp<String>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<String>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/user/login").buildUpon()
        builder.appendQueryParameter("phone", params.phone)
        builder.appendQueryParameter("password", params.password)
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<String> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<String>(exception = e)
            }
            if (resp.exception == null) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(
                    ApiErrorResponse(
                        resp.exception ?: RuntimeException("unknown_error")
                    )
                )
            }
        }
        return liveData
    }
}