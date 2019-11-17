package com.mdove.civilservantcommunity.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment

/**
 * Created by MDove on 2019-11-17.
 */
class EditWeekPlanFragment :BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_week_plan, container, false)
    }
}