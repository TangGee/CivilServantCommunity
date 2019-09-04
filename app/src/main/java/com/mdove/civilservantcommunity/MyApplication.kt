package com.mdove.civilservantcommunity

import android.app.Application
import com.mdove.dependent.common.toast.ToastUtil

/**
 * Created by MDove on 2019-09-04.
 */
class MyApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtil.init(this)
    }
}