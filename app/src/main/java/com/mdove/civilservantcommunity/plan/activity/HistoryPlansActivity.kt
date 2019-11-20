package com.mdove.civilservantcommunity.plan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity
import com.mdove.civilservantcommunity.plan.fragment.HistoryPlansFragment
import com.mdove.civilservantcommunity.plan.viewmodel.HistoryPlansViewModel
import com.mdove.dependent.common.view.calendar.view.Miui10Calendar

/**
 * Created by MDove on 2019-11-06.
 */
class HistoryPlansActivity : AbsSlideCloseActivity() {
    companion object {
        const val TAG_HISTORY_PLAN_FRAGMENT = "history_fragment"

        fun goto(context: Context) {
            val intent = Intent(context, HistoryPlansActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: HistoryPlansViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HistoryPlansViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plans)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    HistoryPlansFragment(),
                    TAG_HISTORY_PLAN_FRAGMENT
                )
                .commit()
        }
    }

    override fun canSlideNow(x: Int, y: Int): Boolean {
        return (supportFragmentManager.findFragmentByTag(TAG_HISTORY_PLAN_FRAGMENT) as? HistoryPlansFragment)?.inCalendarTouchScope(
            x,
            y
        ) == false
    }
}
