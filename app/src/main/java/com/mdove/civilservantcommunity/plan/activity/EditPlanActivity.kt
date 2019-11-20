package com.mdove.civilservantcommunity.plan.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.plan.activity.EditPlanActivity.Companion.INTENT_PARAMS
import com.mdove.civilservantcommunity.plan.fragment.EditPlanContainerFragment
import com.mdove.civilservantcommunity.plan.model.PlanToFeedResult
import com.mdove.civilservantcommunity.plan.model.Status
import com.mdove.civilservantcommunity.plan.viewmodel.EditPlanContainerViewModel


class EditPlanActivity : AbsSlideCloseActivity() {
    companion object {
        private const val TAG_PLAN_FRAGMENT = "tag_plan_fragment"
        const val INTENT_PARAMS = "intent_params_plan"
    }

    private lateinit var containerViewModel: EditPlanContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarTextColorIsBlack(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.content,
                    EditPlanContainerFragment(),
                    TAG_PLAN_FRAGMENT
                )
                .commit()
        }
        containerViewModel = ViewModelProviders.of(this).get(EditPlanContainerViewModel::class.java)
    }

    override fun canSlideNow(x: Int, y: Int): Boolean {
        return containerViewModel.canSlide
    }
}

suspend fun ActivityLauncher.gotoPlanActivity(
    context: Context
): PlanToFeedResult {
    val intent = Intent(context, EditPlanActivity::class.java)
    return startActivityAsync(intent).await().run {
        return if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                PlanToFeedResult(
                    this.data.getParcelableExtra(
                        INTENT_PARAMS
                    ), Status.SUC
                )
            } else {
                PlanToFeedResult(
                    null,
                    Status.CANCEL
                )
            }
        } else {
            PlanToFeedResult(
                null,
                Status.CANCEL
            )
        }
    }
}