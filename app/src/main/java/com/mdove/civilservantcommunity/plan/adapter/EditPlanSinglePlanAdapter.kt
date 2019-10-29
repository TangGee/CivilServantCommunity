package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.civilservantcommunity.plan.PlanModuleBeanWrapper
import com.mdove.civilservantcommunity.plan.PlanModuleType

class EditPlanSinglePlanAdapter : ListAdapter<PlanModuleBeanWrapper, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<PlanModuleBeanWrapper>() {
    override fun areContentsTheSame(
        oldItem: PlanModuleBeanWrapper,
        newItem: PlanModuleBeanWrapper
    ): Boolean {
        return false
    }

    override fun areItemsTheSame(
        oldItem: PlanModuleBeanWrapper,
        newItem: PlanModuleBeanWrapper
    ): Boolean {
        return oldItem === newItem
    }
}) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type == PlanModuleType.CUSTOM_PLAN) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return ModulePlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_plan,
                    parent,
                    false
                )
            )
        } else {
            return ModulePlanCustomViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_custom_plan,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ModulePlanViewHolder)?.let {
            it.bind(data = getItem(position).bean)
        }
    }

    inner class ModulePlanCustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: PlanModuleBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
        }
    }

    inner class ModulePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: PlanModuleBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
        }
    }
}