package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.civilservantcommunity.plan.PlanModuleBeanWrapper
import com.mdove.civilservantcommunity.plan.PlanModuleType
import com.mdove.civilservantcommunity.plan.repository.PlanRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class EditPlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    val data = Transformations.map(repository.getPlans()) {
        val new = mutableListOf<List<PlanModuleBeanWrapper>>()
        // 首个ViewHolder占位
        new.add(mutableListOf())
        it.data?.data?.let {
            new.addAll(it.map {
                it.map {
                    PlanModuleBeanWrapper(it, PlanModuleType.SYS_PALN)
                }.toMutableList().apply {
                    add(PlanModuleBeanWrapper(PlanModuleBean(), PlanModuleType.CUSTOM_PLAN))
                }
            })
        }
        // 用空表示最后一个ok按钮的bean
        new.add(mutableListOf())
        Resource(
            it.status,
            NormalResp<List<List<PlanModuleBeanWrapper>>>(
                it.data?.message ?: "",
                new,
                it.data?.exception
            ),
            it.exception
        )
    }

    fun createFeedPlans(): List<PlanModuleBean> {
        return data.value?.data?.data?.let { data ->
            data.flatMap {
                it.map {
                    it.bean
                }
            }
        } ?: mutableListOf()
    }
}