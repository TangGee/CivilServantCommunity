package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.civilservantcommunity.plan.repository.PlanRepository
import com.mdove.dependent.common.networkenhance.valueobj.Resource

class PlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    val data = Transformations.map(repository.getPlans()) {
        val new = mutableListOf<List<PlanModuleBean>>()
        // 首个ViewHolder占位
        new.add(mutableListOf())
        it.data?.data?.let {
            new.addAll(it)
        }
        // 用空表示最后一个ok按钮的bean
        new.add(mutableListOf())
        Resource(it.status, it.data?.apply {
            data = new
        }, it.exception)
    }

    fun createFeedPlans(): List<PlanModuleBean> {
        return data.value?.data?.data?.let { data ->
            data.flatMap {
                it.map {
                    it
                }
            }
        } ?: mutableListOf()
    }
}