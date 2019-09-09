package com.mdove.civilservantcommunity.detailfeed

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams

/**
 * Created by MDove on 2019-09-09.
 */
class DetailFeedActivity : BaseActivity() {
    companion object{
        const val DETAIL_FEED_ACTIVITY_PARAMS="detail_feed_activity_params"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_feed)
        val params = intent.getParcelableExtra<DetailFeedParams>(DETAIL_FEED_ACTIVITY_PARAMS)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, DetailFeedFragment.newInstance(params)).commit()
        }
    }

    override fun enableTranslucent(): Boolean {
        return false
    }
}