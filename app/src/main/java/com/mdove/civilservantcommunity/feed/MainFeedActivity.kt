package com.mdove.civilservantcommunity.feed

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.content, MainFeedFragment(), "Haha")
                .commit()
        }
    }
}