package com.mdove.civilservantcommunity.ugc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity

/**
 * Created by MDove on 2019-09-15.
 */
class MainUGCActivity : AbsSlideCloseActivity() {
    companion object {
        private const val TAG_UGC_FRAGMENT = "tag_ugc_fragment"

        fun gotoMainUGC(context: Context) {
            val intent = Intent(context, MainUGCActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ugc)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.content, MainUGCFragment(), TAG_UGC_FRAGMENT)
                .commit()
        }
    }
}