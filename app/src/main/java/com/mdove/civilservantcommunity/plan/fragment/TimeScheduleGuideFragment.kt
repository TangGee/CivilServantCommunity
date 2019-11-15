package com.mdove.civilservantcommunity.plan.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.AbsDialogFragment
import kotlinx.android.synthetic.main.fragment_dialog_time_schedule_guide.*

/**
 * Created by MDove on 2019-11-10.
 */
class TimeScheduleGuideFragment : AbsDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_time_schedule_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_content.text = Html.fromHtml(getString(R.string.string_time_schedule_guide))
    }
}