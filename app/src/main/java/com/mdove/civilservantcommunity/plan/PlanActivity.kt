package com.mdove.civilservantcommunity.plan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.plan.PlanActivity.Companion.INTENT_PARAMS

class PlanActivity : AbsSlideCloseActivity() {
    companion object {
        private const val TAG_PLAN_FRAGMENT = "tag_plan_fragment"
        const val INTENT_PARAMS = "intent_params_plan"
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

suspend fun ActivityLauncher.gotoPlanActivity(
    context: Context
): PlanToFeedResult {
    val intent = Intent(context, PlanActivity::class.java)
    return startActivityAsync(intent).await().run {
        return if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                PlanToFeedResult(this.data.getParcelableExtra(INTENT_PARAMS),Status.SUC)
            } else {
                PlanToFeedResult(null, Status.CANCEL)
            }
        } else {
            PlanToFeedResult(null, Status.CANCEL)
        }
    }
}