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

class PlanModulePlanAdapter : ListAdapter<PlanModuleBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<PlanModuleBean>() {
    override fun areContentsTheSame(oldItem: PlanModuleBean, newItem: PlanModuleBean): Boolean {
        return false
    }

    override fun areItemsTheSame(
            oldItem: PlanModuleBean,
            newItem: PlanModuleBean
    ): Boolean {
        return oldItem === newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ModulePlanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_plan_module_plan, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ModulePlanViewHolder)?.let {
            it.bind(data = getItem(position))
        }
    }

    inner class ModulePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: PlanModuleBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
        }
    }
}