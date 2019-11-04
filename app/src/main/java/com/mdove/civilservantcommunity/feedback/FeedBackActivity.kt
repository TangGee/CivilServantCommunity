package com.mdove.civilservantcommunity.feedback

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity

/**
 * Created by MDove on 2019-11-04.
 */
class FeedBackActivity : AbsSlideCloseActivity() {
    companion object {
        const val TAG_FRAGMENT = "tag_feedback_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FeedBackFragment())
        }
    }
}