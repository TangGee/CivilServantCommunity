package com.mdove.civilservantcommunity.detailfeed

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams

/**
 * Created by MDove on 2019-09-09.
 */
class DetailFeedActivity : BaseActivity() {
    companion object {
        const val DETAIL_FEED_ACTIVITY_PARAMS = "detail_feed_activity_params"

        fun gotoFeedDetail(context: Context,params:DetailFeedParams){
            val intent = Intent(context, DetailFeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(DETAIL_FEED_ACTIVITY_PARAMS, params)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
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
}