package com.mdove.civilservantcommunity.plan.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.plan.viewmodel.EditPlanContainerViewModel
import com.mdove.dependent.common.utils.toArrayList
import kotlinx.android.synthetic.main.fragment_edit_plan_container.*

/**
 * Created by MDove on 2019-11-11.
 */
class EditPlanContainerFragment : BaseFragment() {
    private lateinit var viewModel: EditPlanContainerViewModel
    private val titles = mutableListOf("制定每日计划", "制定周计划", "制定双月OKR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(EditPlanContainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_plan_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp.adapter = EditPlanVpAdapter(fragmentManager!!)
        vp.addOnPageChangeListener(onPageChangeListener)
        tab.setViewPager(vp, titles.toArrayList())
        view_toolbar.setTitle("我的规划")
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setColorForAll(Color.BLACK)
    }

    inner class EditPlanVpAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                1 -> EditWeekPlanFragment()
                2 -> EditOKRPlanFragment()
                else -> EditPlanFragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            viewModel.canSlide = state == ViewPager.SCROLL_STATE_IDLE && tab.currentTab == 0
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
        }
    }
}