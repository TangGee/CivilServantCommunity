package com.mdove.civilservantcommunity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mdove.civilservantcommunity.view.utils.StatusBarUtil

/**
 * Created by zhaojing on 2019-09-02.
 */
open class BaseActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucent(this)
    }
}