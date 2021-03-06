package com.mdove.civilservantcommunity.account.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mdove.civilservantcommunity.account.bean.*
import com.mdove.dependent.common.threadpool.MDoveApiPool
import com.mdove.dependent.apiservice.AppDependsProvider
import com.mdove.dependent.common.gson.GsonProvider
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
        builder.appendQueryParameter("user_type", params.userType)
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<RegisterDataResp> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<RegisterDataResp>(exception = e)
            }
            if (resp.isSuc()) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(ApiErrorResponse(RuntimeException(resp.message)))
            }
        }
        return liveData
    }

    fun mePage(uid: String): LiveData<ApiResponse<NormalResp<MePageDataResp>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<MePageDataResp>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/user/check_info").buildUpon()
        builder.appendQueryParameter("uid", uid)
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<MePageDataResp> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<MePageDataResp>(exception = e)
            }
            if (resp.isSuc()) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(ApiErrorResponse(RuntimeException(resp.message)))
            }
        }
        return liveData
    }

    fun login(params: LoginInfoParams): LiveData<ApiResponse<NormalResp<LoginDataResp>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<LoginDataResp>>>()

        val network = AppDependsProvider.networkService
        val builder = Uri.parse("${network.host}/user/login").buildUpon()
        builder.appendQueryParameter("phone", params.phone)
        builder.appendQueryParameter("password", params.password)
        val url = builder.toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json = network.networkClient.get(url)
                val data: NormalResp<LoginDataResp> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<LoginDataResp>(status = 0, exception = e)
            }
            if (resp.isSuc()) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(ApiErrorResponse(RuntimeException(resp.message)))
            }
        }
        return liveData
    }

    fun updateUserInfo(params: UpdateUserInfoParams): LiveData<ApiResponse<NormalResp<String>>> {
        val liveData = MutableLiveData<ApiResponse<NormalResp<String>>>()

        val network = AppDependsProvider.networkService
        val url = Uri.parse("${network.host}/user/update_info").toString()

        CoroutineScope(MDoveApiPool).launch {
            val resp = try {
                val json =
                    network.networkClient.post(url, GsonProvider.getDefaultGson().toJson(params))
                val data: NormalResp<String> = fromServerResp(json)
                data
            } catch (e: Exception) {
                NormalResp<String>(exception = e)
            }
            if (resp.isSuc()) {
                liveData.postValue(ApiSuccessResponse(resp))
            } else {
                liveData.postValue(ApiErrorResponse(RuntimeException(resp.message)))
            }
        }
        return liveData
    }
}