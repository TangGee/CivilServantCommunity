package com.mdove.civilservantcommunity.test

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity

/**
 * Created by MDove on 2019-09-22.
 */
class TestUIActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_ui)
    }

    override fun enableTranslucent(): Boolean {
        return false
    }
}