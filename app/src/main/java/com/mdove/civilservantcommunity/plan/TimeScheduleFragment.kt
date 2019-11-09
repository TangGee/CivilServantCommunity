package com.mdove.civilservantcommunity.plan

import android.app.Activity
import android.content.Intent
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
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDbBean
import com.mdove.civilservantcommunity.plan.model.TimeScheduleParams
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansStatus
import com.mdove.civilservantcommunity.plan.view.OnTimeScheduleLayoutListener
import com.mdove.civilservantcommunity.plan.viewmodel.TimeScheduleViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import com.mdove.dependent.common.view.OnToolbarListener
import kotlinx.android.synthetic.main.fragment_time_schedule.*
import kotlinx.android.synthetic.main.layout_time_schedule.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        time_schedule_layout.setListener(object : OnTimeScheduleLayoutListener {
            override fun onPlansRelease(data: SinglePlanBean) {
                viewModel.releasSinglePlanBean.value = data
            }

            override fun onTouchViewStatusChange(
                data: SinglePlanBean,
                status: TimeSchedulePlansStatus
            ) {
                viewModel.changeSinglePlanBean.value = Pair(data, status)
            }

            override fun onPlansHasAdded(data: SinglePlanBean, planTime: Pair<Long, Long>) {
                viewModel.removeSinglePlanBean.value = data
                viewModel.putOrderTime(data, planTime)
            }
        })
        view_toolbar.setRightBtnTitle("确定")
        view_toolbar.setListener(object : OnToolbarListener {
            override fun onRightBtnClick() {
                activity?.let {
                    launch {
                        showLoading()
                        val params = viewModel.createTimeSchedulePlansToFeed()
                        withContext(MDoveBackgroundPool){
                            // TODO 先查再更新，可以优化
                            MainDb.db.todayPlansDao().getTodayPlansRecord()?.let {
                                // 恶心的重建Entity的过程
                                it.resp = TodayPlansDbBean(it.resp.params.map { moduleBean ->
                                    moduleBean.copy(beanSingles = moduleBean.beanSingles.map { single ->
                                        params.data.find {
                                            it.data.moduleId == single.beanSingle.moduleId &&
                                                    it.data.content == single.beanSingle.content
                                        }?.let {
                                            single.timeSchedule = it.timeSchedule
                                            single
                                        } ?: single
                                    })
                                })
                                MainDb.db.todayPlansDao().update(it)
                            }
                        }
                        dismissLoading()
                        val intent = Intent()
                        intent.putExtra(
                            TAG_TIME_SCHEDULE_PARAMS,
                            params
                        )
                        it.setResult(Activity.RESULT_OK, intent)
                        it.finish()
                    }
                }
            }
        })
        view_toolbar.setTitle("时间管理")
    }
}