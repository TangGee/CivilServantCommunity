package com.mdove.civilservantcommunity.setting

/**
 * Created by MDove on 2020-01-06.
 */
const val SETTING_TYPE_HIDE = 1
const val SETTING_TYPE_CUS = 2

data class SettingItemBean(val title: String, val type: Int = SETTING_TYPE_CUS)