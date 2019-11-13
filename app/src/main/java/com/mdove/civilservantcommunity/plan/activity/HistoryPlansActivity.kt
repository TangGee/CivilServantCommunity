package com.mdove.civilservantcommunity.plan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.plan.fragment.HistoryPlansFragment

/**
 * Created by MDove on 2019-11-06.
 */
class HistoryPlansActivity : BaseActivity() {
    companion object {
        const val TAG_HISTORY_PLAN_FRAGMENT = "history_fragment"

        fun goto(context: Context) {
            val intent = Intent(context, HistoryPlansActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plans)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container,
                    HistoryPlansFragment(),
                    TAG_HISTORY_PLAN_FRAGMENT
                )
                .commit()
        }
    }
}
