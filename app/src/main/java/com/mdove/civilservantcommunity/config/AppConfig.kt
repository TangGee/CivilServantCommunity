package com.mdove.civilservantcommunity.config

import android.content.Context
import android.content.SharedPreferences
import com.mdove.civilservantcommunity.MyApplication
import com.mdove.civilservantcommunity.config.IAppConfig.Companion.KEY_LOGIN_USER_INFO
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.config.IAppConfig.Companion.KEY_SELECT_ROLES
import com.mdove.civilservantcommunity.config.IAppConfig.Companion.KEY_TIME_SCHEDULE_GUIDE
import com.mdove.dependent.common.utils.fromJson
import com.mdove.dependent.common.utils.toJson

/**
 * Created by MDove on 2019-09-08.
 */
object AppConfig : IAppConfig {
    private const val PREFS_FILE = "civil_servant_community"

    private var sPrefs: SharedPreferences? = null

    private fun initSharedPreferences(): SharedPreferences {
        return sPrefs ?: let {
            val temp =
                MyApplication.getInst().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
            sPrefs = temp
            temp
        }
    }

    fun setUserInfo(userInfo: UserInfo?) {
        initSharedPreferences().edit().putString(KEY_LOGIN_USER_INFO, userInfo?.toJson() ?: "")
            .apply()
    }

    fun hasShowTimeScheduleGuide(): Boolean {
        return initSharedPreferences().getBoolean(KEY_TIME_SCHEDULE_GUIDE, false)
    }

    fun setTimeScheduleGuide(show: Boolean) {
        initSharedPreferences().edit().putBoolean(KEY_TIME_SCHEDULE_GUIDE, show)
            .apply()
    }

    fun getUserInfo(): UserInfo? {
        val userInfoJson = initSharedPreferences().getString(KEY_LOGIN_USER_INFO, "") ?: ""
        return try {
            fromJson<UserInfo>(userInfoJson)
        } catch (e: Exception) {
            null
        }
    }

    fun setSelectRoles(hasSelect: Boolean) {
        initSharedPreferences().edit().putBoolean(KEY_SELECT_ROLES, hasSelect)
            .apply()
    }

    fun hasSelectRoles(): Boolean {
        return initSharedPreferences().getBoolean(KEY_SELECT_ROLES, false)
    }
}