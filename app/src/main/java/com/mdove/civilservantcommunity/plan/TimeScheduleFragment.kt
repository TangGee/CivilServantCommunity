package com.mdove.civilservantcommunity.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.plan.TimeScheduleActivity.Companion.TAG_TIME_SCHEDULE_PARAMS
import com.mdove.civilservantcommunity.plan.model.TimeScheduleParams
import com.mdove.civilservantcommunity.plan.viewmodel.TimeScheduleViewModel
import kotlinx.android.synthetic.main.fragment_time_schedule.*

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleFragment : BaseFragment() {
    private lateinit var viewModel: TimeScheduleViewModel

    companion object {
        fun newInstance(params: TimeScheduleParams): Fragment =
            TimeScheduleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TAG_TIME_SCHEDULE_PARAMS, params)
                    classLoader = params::class.java.classLoader
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(TimeScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<TimeScheduleParams>(TAG_TIME_SCHEDULE_PARAMS)?.let {
            viewModel.paramsLiveData.value = it
        } ?: also {
            activity?.finish()
        }
        viewModel.plansLiveData.observe(this, Observer {
            time_schedule_layout.updatePlans(it)
        })
    }
}