package com.mdove.civilservantcommunity.plan.viewmodel

import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.plan.repository.PlanRepository

class PlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    val data = repository.getPlans()
}