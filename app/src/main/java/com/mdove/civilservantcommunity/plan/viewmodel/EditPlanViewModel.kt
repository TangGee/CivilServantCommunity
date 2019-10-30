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
    val deletePlanModuleLiveData = MutableLiveData<PlanModuleBean>()

    val data = MediatorLiveData<Resource<NormalResp<List<PlanModuleBean>>>>().apply {
        addSource(repository.getPlans()) {
            val new = mutableListOf<PlanModuleBean>()
            // 首个ViewHolder占位
            new.add(PlanModuleBean("padding", "padding", mutableListOf(), PlanModuleType.PADDING))
            it.data?.data?.let { rawList ->
                rawList.forEach {
                    val moduleId = it.firstOrNull()?.moduleId
                    val moduleName = it.firstOrNull()?.moduleName
                    if (moduleId.isNullOrBlank() || moduleName.isNullOrBlank()) {
                        return@forEach
                    }
                    new.add(PlanModuleBean(moduleId, moduleName,
                        it.map { SinglePlanBeanWrapper(it, SinglePlanType.SYS_PLAN) }
                            .toMutableList()
                            .apply {
                                add(
                                    SinglePlanBeanWrapper(
                                        SinglePlanBean(),
                                        SinglePlanType.CUSTOM_PLAN
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
    }

    fun createFeedPlans(): List<PlanModuleBean> {
        return data.value?.data?.data?.filter {
            it.moduleType == PlanModuleType.NORMAL
        } ?: mutableListOf()
    }
}