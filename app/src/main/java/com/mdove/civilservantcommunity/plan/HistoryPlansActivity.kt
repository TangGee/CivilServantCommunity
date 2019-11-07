package com.mdove.civilservantcommunity.plan

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity

/**
 * Created by MDove on 2019-11-06.
 */
class HistoryPlansActivity : AbsSlideCloseActivity() {
    companion object {
        const val TAG_HISTORY_PLAN_FRAGMENT = "history_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_plans)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, PlanFragment(), TAG_HISTORY_PLAN_FRAGMENT)
                .commit()
        }
    }
}
