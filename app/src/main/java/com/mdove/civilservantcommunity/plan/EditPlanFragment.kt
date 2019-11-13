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
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.plan.adapter.EditPlanModuleAdapter
import com.mdove.civilservantcommunity.plan.adapter.OnPlanModuleClickListener
import com.mdove.civilservantcommunity.plan.adapter.OnSinglePlanClickListener
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDbBean
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.viewmodel.EditPlanViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.recyclerview.PaddingDecoration
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_plan.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlanFragment : BaseFragment() {
    private lateinit var mViewModelEdit: EditPlanViewModel
    private val adapter = EditPlanModuleAdapter(object : OnPlanModuleClickListener {
        override fun onClickTimeSchedule() {
            context?.let {
                TimeScheduleActivity.goto(it, mViewModelEdit.createTimeScheduleParams())
            }
        }

        override fun onDeletePlanModuleClick(data: PlanModuleBean, delete: Boolean) {
            mViewModelEdit.deletePlanModuleLiveData.value = Pair(data, delete)
        }

        override fun onClickCreatePlans() {
            activity?.let {
                if (it.isFinishing) {
                    return@let
                }
                launch {
                    showLoading()
                    val plans = mViewModelEdit.createFeedPlans()
                    if (plans.isNullOrEmpty()) {
                        ToastUtil.toast("执行空计划，这个不太好吧~")
                        return@launch
                    }
                    val insertData = System.currentTimeMillis()
                    val createData = TimeUtils.getDateFromSQL()
                    val dbId = saveTodayPlans(insertData, createData, plans)
                    dismissLoading()
                    val intent = Intent()
                    intent.putExtra(
                        EditPlanActivity.INTENT_PARAMS,
                        PlanToFeedParams(dbId, insertData, createData, null, plans)
                    )
                    it.setResult(Activity.RESULT_OK, intent)
                    it.finish()
                }
            }
        }
    }, object : OnSinglePlanClickListener {
        override fun onEditSinglePlan(data: SinglePlanBean, editStr: String) {
            mViewModelEdit.editSinglePlanLiveData.value = Pair(data, editStr)
        }

        override fun onDeleteSinglePlanClick(data: SinglePlanBean, delete: Boolean) {
            mViewModelEdit.deleteSinglePlanLiveData.value = Pair(data, delete)
        }

        override fun onCustomClick(data: SinglePlanBean) {
            mViewModelEdit.customSinglePlanLiveData.value = data
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
        rlv.addItemDecoration(PaddingDecoration(12))
        mViewModelEdit.data.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let {
                        adapter.submitList(it)
                        rlv.updateEmptyUI()
                    }
                }
                Status.LOADING -> {
                    rlv.updateEmptyUI()
                }
            }
        })
    }

    private suspend fun saveTodayPlans(
        insertData: Long,
        createData: String,
        plans: List<PlanModuleBean>
    ): Long = withContext(MDoveBackgroundPool) {
        // 修计划时，先删了之前当天的计划
        MainDb.db.todayPlansDao().getTodayPlansRecord(TimeUtils.getDateFromSQL())?.let {
            MainDb.db.todayPlansDao().deleteTodayPlanRecord(it)
        }
        MainDb.db.todayPlansDao().insert(
            TodayPlansEntity(
                date = insertData,
                sucDate = null,
                createDate = createData,
                resp = TodayPlansDbBean(plans.map { module ->
                    module.copy(beanSingles = module.beanSingles.filterNot { single ->
                        single.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
                    })
                })
            )
        ) ?: -1
    }
}