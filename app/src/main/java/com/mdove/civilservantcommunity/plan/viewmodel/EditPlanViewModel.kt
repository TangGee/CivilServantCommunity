package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.civilservantcommunity.plan.repository.PlanRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class EditPlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    val deleteSinglePlanLiveData = MutableLiveData<Pair<SinglePlanBean, Boolean>>()
    val customSinglePlanLiveData = MutableLiveData<SinglePlanBean>()
    val deletePlanModuleLiveData = MutableLiveData<Pair<PlanModuleBean, Boolean>>()
    val editSinglePlanLiveData = MutableLiveData<Pair<SinglePlanBean, String>>()

    val data = MediatorLiveData<Resource<NormalResp<List<PlanModuleBean>>>>().apply {
        addSource(repository.getPlans()) {
            val new = mutableListOf<PlanModuleBean>()
            // 首个ViewHolder占位
            new.add(
                PlanModuleBean(
                    "padding",
                    "padding",
                    mutableListOf(),
                    PlanModuleType.PADDING,
                    PlanModuleStatus.NORMAL
                )
            )
            it.data?.data?.let { rawList ->
                new.add(
                    PlanModuleBean(
                        "edit_plans",
                        "edit_plans",
                        mutableListOf(),
                        PlanModuleType.EDIT_PLANS_TIPS,
                        PlanModuleStatus.NORMAL
                    )
                )
                rawList.forEach {
                    val firstSinglePlan = it.firstOrNull()?.beanSingle ?: return@forEach
                    new.add(
                        PlanModuleBean(
                            firstSinglePlan.moduleId!!, firstSinglePlan.moduleName!!,
                            it.toMutableList()
                                .apply {
                                    add(
                                        SinglePlanBeanWrapper(
                                            firstSinglePlan,
                                            SinglePlanType.CUSTOM_PLAN_BTN
                                        )
                                    )
                                }, PlanModuleType.NORMAL
                            , PlanModuleStatus.NORMAL
                        )
                    )
                }
            } ?: also {
                new.add(
                    PlanModuleBean(
                        "create_plans",
                        "create_plans",
                        mutableListOf(),
                        PlanModuleType.CREATE_PLANS_TIPS,
                        PlanModuleStatus.NORMAL
                    )
                )
            }
            new.add(
                PlanModuleBean(
                    "btn_ok",
                    "btn_ok",
                    mutableListOf(),
                    PlanModuleType.BTN_OK,
                    PlanModuleStatus.NORMAL
                )
            )
            new.add(
                PlanModuleBean(
                    "btn_time_schedule",
                    "btn_time_schedule",
                    mutableListOf(),
                    PlanModuleType.BTN_TIME_SCHEDULE,
                    PlanModuleStatus.NORMAL
                )
            )
            value = Resource(
                it.status,
                NormalResp<List<PlanModuleBean>>(
                    it.data?.message ?: "",
                    new,
                    it.data?.exception
                ),
                it.exception
            )
        }

        // 删除某个具体计划
        addSource(deleteSinglePlanLiveData) { deletePair ->
            value = value?.let {
                Resource(it.status, data = it.data?.copy(data = it.data?.data?.map {
                    var changeVersion = it.changeVersion
                    val plans = it.beanSingles.map {
                        if (deletePair.first.moduleId == it.beanSingle.moduleId && deletePair.first.content == it.beanSingle.content) {
                            changeVersion++
                            it.copy(statusSingle = if (deletePair.second) SinglePlanStatus.DELETE else SinglePlanStatus.NORMAL)
                        } else {
                            it
                        }
                    }
                    it.copy(beanSingles = plans, changeVersion = changeVersion)
                }), exception = it.exception)
            }
        }

        // 删除整个模块计划
        addSource(deletePlanModuleLiveData) { deleteModule ->
            value = value?.let {
                Resource(it.status, data = it.data?.copy(data = it.data?.data?.map {
                    if (it.moduleId == deleteModule.first.moduleId) {
                        it.copy(moduleStatus = if (deleteModule.second) PlanModuleStatus.DELETE else PlanModuleStatus.NORMAL)
                    } else {
                        it
                    }
                }), exception = it.exception)
            }
        }

        // 修改一个系统计划
        addSource(editSinglePlanLiveData) { edit ->
            value = value?.let {
                Resource(it.status, data = it.data?.copy(data = it.data?.data?.map {
                    var changeVersion = it.changeVersion
                    if (it.moduleId == edit.first.moduleId) {
                        it.copy(beanSingles = it.beanSingles.map { single ->
                            if (single.beanSingle.content == edit.first.content) {
                                changeVersion++
                                single.copy(beanSingle = single.beanSingle.copy(content = edit.second))
                            } else {
                                single
                            }
                        }, changeVersion = changeVersion)
                    } else {
                        it
                    }
                }), exception = it.exception)
            }
        }

        // 增加一个自定义计划
        addSource(customSinglePlanLiveData) { custom ->
            value = value?.let { res ->
                Resource(
                    res.status,
                    data = res.data?.copy(data = res.data?.data?.map { planModule ->
                        planModule.beanSingles.takeIf {
                            // 先判断添加到哪一个module
                            it.find {
                                custom.moduleId == it.beanSingle.moduleId && it.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
                            } != null
                        }?.let {
                            planModule.copy(beanSingles = it.filterNot { single ->
                                custom.moduleId == single.beanSingle.moduleId && single.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
                            }.toMutableList().apply {
                                add(
                                    SinglePlanBeanWrapper(
                                        custom,
                                        SinglePlanType.CUSTOM_PLAN
                                    )
                                )
                                add(
                                    SinglePlanBeanWrapper(
                                        custom.copy(content = ""),
                                        SinglePlanType.CUSTOM_PLAN_BTN
                                    )
                                )
                            })
                        } ?: let {
                            planModule
                        }
                    }),
                    exception = res.exception
                )
            }
        }
    }

    fun createFeedPlans(): List<PlanModuleBean> {
        val plans = mutableListOf<PlanModuleBean>()
        data.value?.data?.data?.filter {
            it.moduleType == PlanModuleType.NORMAL && it.moduleStatus != PlanModuleStatus.DELETE
        }?.forEach {
            plans.add(it.copy(beanSingles = it.beanSingles.filter {
                (it.typeSingle == SinglePlanType.CUSTOM_PLAN || it.typeSingle == SinglePlanType.SYS_PLAN)
                        && it.statusSingle != SinglePlanStatus.DELETE
            }))
        }
        return plans
    }

    fun createTimeScheduleParams(): TimeScheduleParams {
        return TimeScheduleParams(createFeedPlans().flatMap {
            it.beanSingles.map { wrapper ->
                TimeSchedulePlansParams(
                    wrapper.beanSingle,
                    TimeSchedulePlansStatus.SHOW,
                    wrapper.timeSchedule
                )
            }
        })
    }
}