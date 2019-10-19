package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.PlanModuleModel

class PlanModuleAdapter : ListAdapter<PlanModuleModel, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<PlanModuleModel>() {
    override fun areContentsTheSame(oldItem: PlanModuleModel, newItem: PlanModuleModel): Boolean {
        return false
    }

    override fun areItemsTheSame(
            oldItem: PlanModuleModel,
            newItem: PlanModuleModel
    ): Boolean {
        return oldItem === newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlanModuleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_plan_module, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PlanModuleViewHolder)?.let {
            it.bind(data = getItem(position))
        }
    }

    inner class PlanModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: PlanModuleModel) {
            itemView.findViewById<RecyclerView>(R.id.rlv).apply {
                this.layoutManager = LinearLayoutManager(itemView.context)
                this.adapter = PlanModulePlanAdapter().apply {
                    this.submitList(data.data)
                }
            }
        }
    }
}