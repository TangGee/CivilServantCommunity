package com.mdove.civilservantcommunity.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.dependent.common.recyclerview.ViewPagerLayoutManager
import kotlinx.android.synthetic.main.fragment_history_plan.*

/**
 * Created by MDove on 2019-11-06.
 */
class HistoryPlansFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlv.layoutManager = ViewPagerLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
    }
}