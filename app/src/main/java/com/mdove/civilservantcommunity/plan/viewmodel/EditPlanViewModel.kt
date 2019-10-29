package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.*
import com.mdove.civilservantcommunity.plan.repository.PlanRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class EditPlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    val data = Transformations.map(repository.getPlans()) {
        val new = mutableListOf<PlanModuleBean>()
        // 首个ViewHolder占位
        new.add(PlanModuleBean(mutableListOf(), PlanModuleType.PADDING))
        it.data?.data?.let { rawList ->
            rawList.forEach {
                new.add(PlanModuleBean(it.map {
                    SinglePlanBeanWrapper(it, SinglePlanType.SYS_PLAN)
                }.toMutableList().apply {
                    add(SinglePlanBeanWrapper(SinglePlanBean(), SinglePlanType.CUSTOM_PLAN))
                }, PlanModuleType.NORMAL))
            }
        }
        // 用空表示最后一个ok按钮的bean
        new.add(PlanModuleBean(mutableListOf(), PlanModuleType.BTN_OK))
        Resource(
            it.status,
            NormalResp<List<PlanModuleBean>>(
                it.data?.message ?: "",
                new,
                it.data?.exception
            ),
            it.exception
        )
    }

    fun createFeedPlans(): List<SinglePlanBean> {
        return data.value?.data?.data?.let { data ->
            data.flatMap {
                it.beanSingles.map {
                    it.beanSingle
                }
            }
        } ?: mutableListOf()
    }
}