package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.plan.SinglePlanBeanWrapper
import com.mdove.civilservantcommunity.plan.SinglePlanType

class EditSinglePlanAdapter : ListAdapter<SinglePlanBeanWrapper, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<SinglePlanBeanWrapper>() {
    override fun areContentsTheSame(
        oldItem: SinglePlanBeanWrapper,
        newItem: SinglePlanBeanWrapper
    ): Boolean {
        return false
    }

    override fun areItemsTheSame(
        oldItem: SinglePlanBeanWrapper,
        newItem: SinglePlanBeanWrapper
    ): Boolean {
        return oldItem === newItem
    }
}) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).typeSingle == SinglePlanType.CUSTOM_PLAN) {
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
            it.bind(data = getItem(position).beanSingle)
        }
    }

    inner class ModulePlanCustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: SinglePlanBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
        }
    }

    inner class ModulePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: SinglePlanBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
        }
    }
}