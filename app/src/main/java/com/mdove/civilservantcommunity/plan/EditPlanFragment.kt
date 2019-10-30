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
import com.mdove.civilservantcommunity.plan.adapter.EditPlanModuleAdapter
import com.mdove.civilservantcommunity.plan.adapter.OnPlanModuleClickListener
import com.mdove.civilservantcommunity.plan.adapter.OnSinglePlanClickListener
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDbBean
import com.mdove.civilservantcommunity.plan.viewmodel.EditPlanViewModel
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
    private lateinit var mViewModelEdit: EditPlanViewModel
    private val adapter = EditPlanModuleAdapter(object : OnPlanModuleClickListener {
        override fun onDeletePlanModuleClick(data: PlanModuleBean) {
            mViewModelEdit.deletePlanModuleLiveData.value = data
        }

        override fun onClickCreatePlans() {
            activity?.let {
                if (it.isFinishing) {
                    return@let
                }
                launch {
                    showLoading()
                    val plans = mViewModelEdit.createFeedPlans()
                    withContext(MDoveBackgroundPool) {
                        MainDb.db.todayPlansDao().insert(
                            TodayPlansEntity(
                                date = System.currentTimeMillis(),
                                sucDate = null,
                                createDate = TimeUtils.getDateFromSQL(),
                                resp = TodayPlansDbBean(plans.map {
                                    it.copy(beanSingles = it.beanSingles.filterNot {
                                        it.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
                                    })
                                })
                            )
                        )
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
    }, object : OnSinglePlanClickListener {
        override fun onDeleteSinglePlanClick(data: SinglePlanBean) {
            mViewModelEdit.deleteSinglePlanLiveData.value = data
        }

        override fun onCustomClick(data: SinglePlanBean) {
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mViewModelEdit = ViewModelProviders.of(it).get(EditPlanViewModel::class.java)
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
        mViewModelEdit.data.observe(this, Observer {
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