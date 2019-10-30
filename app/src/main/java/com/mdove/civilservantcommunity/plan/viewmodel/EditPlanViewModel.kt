package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.*
import com.mdove.civilservantcommunity.plan.repository.PlanRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class EditPlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    val deleteSinglePlanLiveData = MutableLiveData<SinglePlanBean>()
    val customSinglePlanLiveData = MutableLiveData<SinglePlanBean>()
    val deletePlanModuleLiveData = MutableLiveData<PlanModuleBean>()

    val data = MediatorLiveData<Resource<NormalResp<List<PlanModuleBean>>>>().apply {
        addSource(repository.getPlans()) {
            val new = mutableListOf<PlanModuleBean>()
            // 首个ViewHolder占位
            new.add(PlanModuleBean("padding", "padding", mutableListOf(), PlanModuleType.PADDING))
            it.data?.data?.let { rawList ->
                rawList.forEach {
                    val firstSinglePlan = it.firstOrNull()
                    val moduleId = it.firstOrNull()?.moduleId
                    val moduleName = it.firstOrNull()?.moduleName
                    if (moduleId.isNullOrBlank() || moduleName.isNullOrBlank() || firstSinglePlan == null) {
                        return@forEach
                    }
                    new.add(
                        PlanModuleBean(
                            moduleId, moduleName,
                            it.map {
                                SinglePlanBeanWrapper(it, SinglePlanType.SYS_PLAN)
                            }.toMutableList()
                                .apply {
                                    add(
                                        SinglePlanBeanWrapper(
                                            firstSinglePlan.copy(content = ""),
                                            SinglePlanType.CUSTOM_PLAN_BTN
                                        )
                                    )
                                }, PlanModuleType.NORMAL
                        )
                    )
                }
            }
            // 用空表示最后一个ok按钮的bean
            new.add(PlanModuleBean("btn_ok", "btn_ok", mutableListOf(), PlanModuleType.BTN_OK))
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
        addSource(deleteSinglePlanLiveData) { delete ->
            value = value?.let {
                Resource(it.status, data = it.data?.copy(data = it.data?.data?.map {
                    it.copy(beanSingles = it.beanSingles.filterNot {
                        delete.moduleId == it.beanSingle.moduleId && delete.content == it.beanSingle.content
                    })
                }), exception = it.exception)
            }
        }

        // 删除整个模块计划
        addSource(deletePlanModuleLiveData) { deleteModule ->
            value = value?.let {
                Resource(it.status, data = it.data?.copy(data = it.data?.data?.filterNot {
                    it.moduleId == deleteModule.moduleId
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
                                add(SinglePlanBeanWrapper(custom, SinglePlanType.CUSTOM_PLAN))
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
            it.moduleType == PlanModuleType.NORMAL
        }?.forEach {
            plans.add(it.copy(beanSingles = it.beanSingles.filter {
                it.typeSingle == SinglePlanType.CUSTOM_PLAN || it.typeSingle == SinglePlanType.SYS_PLAN
            }))
        }
        return plans
    }
}