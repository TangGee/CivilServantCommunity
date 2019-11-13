package com.mdove.civilservantcommunity.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.AbsDialogFragment

/**
 * Created by MDove on 2019-11-10.
 */
class TimeScheduleGuideFragment :AbsDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_time_schedule_guide, container, false)
    }
}