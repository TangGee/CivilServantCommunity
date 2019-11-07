package com.mdove.civilservantcommunity.plan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.plan.model.TimeScheduleParams

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_plans)
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
