package com.mdove.civilservantcommunity.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity

/**
 * Created by MDove on 2019-10-27.
 */
class MePageActivity : AbsSlideCloseActivity() {
    companion object {
        const val TAG_ME_PAGE_FRAGMENT = "tag_me_page_fragment"

        fun gotoMePage(context: Context) {
            val intent = Intent(context, MePageActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_me_page)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MePageFragment(), TAG_ME_PAGE_FRAGMENT)
                .commit()
        }
    }
}