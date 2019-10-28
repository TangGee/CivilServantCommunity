package com.mdove.civilservantcommunity.plan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.plan.adapter.PlanModuleAdapter
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.viewmodel.PlanViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_plan.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlanFragment : BaseFragment() {
    private lateinit var viewModel: PlanViewModel
    private val adapter = PlanModuleAdapter(object : OnPlanClickListener {
        override fun onClick(type: Int) {
            activity?.let {
                if (!it.isFinishing) {
                    launch {
                        showLoading()
                        val plans = viewModel.createFeedPlans()
                        withContext(MDoveBackgroundPool) {
                            plans.forEach {
                                MainDb.db.todayPlansDao().insert(
                                    TodayPlansEntity(
                                        date = System.currentTimeMillis(),
                                        createDate = TimeUtils.getDateFromSQL(),
                                        resp = FeedTimeLineFeedTodayPlansResp(it)
                                    )
                                )
                            }
                        }
                        dismissLoading()
                        val intent = Intent()
                        intent.putExtra(
                            PlanActivity.INTENT_PARAMS,
                            PlanToFeedParams(plans)
                        )
                        it.setResult(Activity.RESULT_OK, intent)
                        it.finish()
                    }
                }
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(PlanViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_toolbar.setTitle("我的计划")
        rlv.layoutManager = LinearLayoutManager(context)
        rlv.adapter = adapter
        viewModel.data.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let {
                        adapter.submitList(it)
                    }
                }
            }
        })
    }
}

interface OnPlanClickListener {
    fun onClick(type: Int)
}