package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.base.adapter.NormalErrorIconBean
import com.mdove.civilservantcommunity.base.adapter.TYPE_NORMAL_ERROR_ICON
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.civilservantcommunity.plan.repository.PlanRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.networkenhance.valueobj.Status

class EditPlanViewModel : ViewModel() {
    companion object {
        const val CONST_MODULE_ID = "edit_plan_custom"
        const val CONST_MODULE_NAME = "自定义"
    }

    private val repository = PlanRepository()

    val deleteSinglePlanLiveData = MutableLiveData<Pair<SinglePlanBean, Boolean>>()
    val customSinglePlanLiveData = MutableLiveData<SinglePlanBean>()
    val deletePlanModuleLiveData = MutableLiveData<Pair<PlanModuleBean, Boolean>>()
    val editSinglePlanLiveData = MutableLiveData<Pair<SinglePlanBean, String>>()
    val customPlans = MutableLiveData<String>()

    val data = MediatorLiveData<Resource<NormalResp<List<PlanModuleBean>>>>().apply {
        addSource(repository.getPlans()) {
            val new = mutableListOf<PlanModuleBean>()
            it.data?.data?.firstOrNull()?.firstOrNull()?.takeIf {
                it.typeSingle == SinglePlanType.SYS_PLAN
            }?.let {
                // 首个ViewHolder占位
                new.add(
                    PlanModuleBean(
                        "score",
                        "score",
                        mutableListOf(),
                        PlanModuleType.SCORE,
                        PlanModuleStatus.NORMAL
                    )
                )
                new.add(
                    PlanModuleBean(
                        "edit_plans",
                        "edit_plans",
                        mutableListOf(),
                        PlanModuleType.EDIT_PLANS_TIPS,
                        PlanModuleStatus.NORMAL
                    )
                )
            }
            it.data?.data?.let { respData ->
                respData.forEach {
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

            if (it.status == Status.ERROR) {
                new.add(
                    PlanModuleBean(
                        "btn_edit_custom",
                        "btn_edit_custom",
                        mutableListOf(),
                        PlanModuleType.BTN_EDIT_CUSTOM,
                        PlanModuleStatus.NORMAL
                    )
                )
                new.add(
                    PlanModuleBean(
                        "error_icon",
                        "error_icon",
                        mutableListOf(),
                        PlanModuleType.ERROR_ICON,
                        PlanModuleStatus.NORMAL
                    )
                )
                new.add(
                    PlanModuleBean(
                        "error_title",
                        "error_title",
                        mutableListOf(),
                        PlanModuleType.ERROR_TITLE,
                        PlanModuleStatus.NORMAL
                    )
                )
            }

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

        // 增加一个自定义分类的自定义计划
        addSource(customPlans) { custom ->
            value = value?.let {
                Resource(it.status, data = it.data?.copy(data = it.data?.data?.let { data ->
                    val realData = mutableListOf<PlanModuleBean>()
                    data.find { bean ->
                        bean.moduleId == CONST_MODULE_ID
                    }?.let { findBean ->
                        val userInfo = AppConfig.getUserInfo()
                        data.forEach {
                            if (it.moduleId == findBean.moduleId) {
                                realData.add(it.copy(beanSingles = it.beanSingles.toMutableList().apply {
                                    add(
                                        SinglePlanBeanWrapper(
                                            SinglePlanBean(
                                                userInfo?.uid,
                                                CONST_MODULE_ID,
                                                CONST_MODULE_NAME,
                                                content = custom
                                            ),
                                            SinglePlanType.CUSTOM_PLAN
                                        )
                                    )
                                }))
                            } else {
                                realData.add(it)
                            }
                        }

                    } ?: let {
                        val userInfo = AppConfig.getUserInfo()
                        realData.addAll(data.filterNot {
                            it.moduleType == PlanModuleType.ERROR_TITLE || it.moduleType == PlanModuleType.ERROR_ICON
                        }.toMutableList().apply {
                            add(
                                PlanModuleBean(
                                    CONST_MODULE_ID,
                                    CONST_MODULE_NAME,
                                    mutableListOf(
                                        SinglePlanBeanWrapper(
                                            SinglePlanBean(
                                                userInfo?.uid,
                                                CONST_MODULE_ID,
                                                CONST_MODULE_NAME,
                                                content = custom
                                            ),
                                            SinglePlanType.CUSTOM_PLAN
                                        )
                                    )
                                )
                            )
                        })
                        realData.add(
                            PlanModuleBean(
                                "btn_ok",
                                "btn_ok",
                                mutableListOf(),
                                PlanModuleType.BTN_OK,
                                PlanModuleStatus.NORMAL
                            )
                        )
                        realData.add(
                            PlanModuleBean(
                                "btn_time_schedule",
                                "btn_time_schedule",
                                mutableListOf(),
                                PlanModuleType.BTN_TIME_SCHEDULE,
                                PlanModuleStatus.NORMAL
                            )
                        )
                    }
                    realData
                }), exception = it.exception)
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