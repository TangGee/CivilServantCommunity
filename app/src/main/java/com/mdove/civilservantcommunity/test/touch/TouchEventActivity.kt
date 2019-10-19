package com.mdove.civilservantcommunity.test.touch

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity

class TouchEventActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_event)
    }

    override fun enableTranslucent(): Boolean {
        return false
    }
}