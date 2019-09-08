package com.mdove.civilservantcommunity.config

import android.content.Context
import android.content.SharedPreferences
import com.mdove.civilservantcommunity.MyApplication
import com.mdove.civilservantcommunity.config.IAppConfig.Companion.KEY_LOGIN_USER_INFO
import com.mdove.civilservantcommunity.login.bean.LoginInfoParams
import com.mdove.civilservantcommunity.login.bean.UserInfo
import com.mdove.dependent.common.utils.fromJson
import com.mdove.dependent.common.utils.toJson

/**
 * Created by MDove on 2019-09-08.
 */
object AppConfig : IAppConfig {
    private val PREFS_FILE = "civil_servant_community"

    private var sPrefs: SharedPreferences? = null

    private fun initSharedPreferences(): SharedPreferences {
        return sPrefs ?: let {
            val temp =
                MyApplication.getInst().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
            sPrefs = temp
            temp
        }
    }

    fun setUserInfo(userInfo: UserInfo) {
        initSharedPreferences().edit().putString(KEY_LOGIN_USER_INFO, userInfo.toJson()).apply()
    }

    fun getUserInfo(): UserInfo? {
        val userInfoJson = initSharedPreferences().getString(KEY_LOGIN_USER_INFO, "") ?: ""
        return try {
            fromJson<UserInfo>(userInfoJson)
        } catch (e: Exception) {
            null
        }
    }
}