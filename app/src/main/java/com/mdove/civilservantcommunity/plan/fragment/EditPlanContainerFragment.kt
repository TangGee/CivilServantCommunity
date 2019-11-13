package com.mdove.civilservantcommunity.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.plan.viewmodel.EditPlanContainerViewModel

/**
 * Created by MDove on 2019-11-11.
 */
class EditPlanContainerFragment : BaseFragment() {
    private lateinit var viewModel: EditPlanContainerViewModel

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
//        vp.adapter = object : FragmentPagerAdapter(childFragmentManager) {
//            override fun getItem(position: Int): Fragment {
//                return if (position == 0) {
//                    EditPlanFragment()
//                } else {
//                    viewModel.params?.let {
//                        TimeScheduleFragment.newInstance(it)
//                    }
//                }
//            }
//
//            override fun getCount(): Int {
//                return 2
//            }
//        }
    }
}