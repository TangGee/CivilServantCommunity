package com.mdove.civilservantcommunity.plan.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.plan.activity.TimeScheduleActivity.Companion.TAG_TIME_SCHEDULE_PARAMS
import com.mdove.civilservantcommunity.plan.fragment.TimeScheduleFragment
import com.mdove.civilservantcommunity.plan.model.TimeScheduleParams
import com.mdove.civilservantcommunity.plan.model.TimeScheduleStatus
import com.mdove.civilservantcommunity.plan.model.TimeScheduleToFeedResult

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleActivity : BaseActivity() {
    companion object {
        const val TAG_TIME_SCHEDULE_FRAGMENT = "time_schedule_fragment"
        const val TAG_TIME_SCHEDULE_PARAMS = "time_schedule_params"

        fun goto(context: Context, params: TimeScheduleParams) {
            val intent = Intent(context, TimeScheduleActivity::class.java)
            intent.putExtra(TAG_TIME_SCHEDULE_PARAMS, params)
            context.startActivity(intent)
        }
    }

    override var animType: Int = TYPE_ANIM_UP_DOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plans)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    TimeScheduleFragment.newInstance(
                        intent.getParcelableExtra(TAG_TIME_SCHEDULE_PARAMS)
                    ),
                    TAG_TIME_SCHEDULE_FRAGMENT
                )
                .commit()
        }
    }
}

suspend fun ActivityLauncher.gotoTimeScheduleActivity(
    context: Context,
    params: TimeScheduleParams
): TimeScheduleToFeedResult {
    val intent = Intent(context, TimeScheduleActivity::class.java)
    intent.putExtra(TAG_TIME_SCHEDULE_PARAMS, params)
    return startActivityAsync(intent).await().run {
        return if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                TimeScheduleToFeedResult(
                    this.data.getParcelableExtra(TAG_TIME_SCHEDULE_PARAMS),
                    TimeScheduleStatus.SUC
                )
            } else {
                TimeScheduleToFeedResult(
                    null,
                    TimeScheduleStatus.CANCEL
                )
            }
        } else {
            TimeScheduleToFeedResult(null, TimeScheduleStatus.ERROR)
        }
    }
}
