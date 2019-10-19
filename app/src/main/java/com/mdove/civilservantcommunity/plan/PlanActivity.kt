package com.mdove.civilservantcommunity.plan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity

class PlanActivity :BaseActivity() {
    companion object {
        private const val TAG_PLAN_FRAGMENT = "tag_plan_fragment"

        fun gotoPlan(context: Context) {
            val intent = Intent(context, PlanActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, PlanFragment(), TAG_PLAN_FRAGMENT)
                    .commit()
        }
    }
}