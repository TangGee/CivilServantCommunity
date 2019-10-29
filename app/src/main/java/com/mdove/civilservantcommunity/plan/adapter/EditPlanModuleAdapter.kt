package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.OnPlanClickListener
import com.mdove.civilservantcommunity.plan.PlanModuleBeanWrapper

class EditPlanModuleAdapter(private val listener: OnPlanClickListener) :
    ListAdapter<List<PlanModuleBeanWrapper>, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<List<PlanModuleBeanWrapper>>() {
        override fun areContentsTheSame(
            oldItem: List<PlanModuleBeanWrapper>,
            newItem: List<PlanModuleBeanWrapper>
        ): Boolean {
            return false
        }

        override fun areItemsTheSame(
            oldItem: List<PlanModuleBeanWrapper>,
            newItem: List<PlanModuleBeanWrapper>
        ): Boolean {
            return oldItem === newItem
        }

    }) {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            2
        } else if (getItem(position).isEmpty() || itemCount - 1 == position) {
            1
        } else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            PlanModuleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module,
                    parent,
                    false
                )
            )
        } else if (viewType == 2) {
            PaddingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_rlv_padding,
                    parent,
                    false
                )
            )
        } else {
            PlanModuleOkViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_ok,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PlanModuleViewHolder)?.let {
            it.bind(data = getItem(position))
        }
    }

    inner class PlanModuleOkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onClick(0)
            }
        }
    }

    inner class PaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class PlanModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: List<PlanModuleBeanWrapper>) {
            itemView.findViewById<TextView>(R.id.tv_module_name).text = data[0].bean.moduleName
            itemView.findViewById<RecyclerView>(R.id.rlv).apply {
                this.layoutManager = LinearLayoutManager(this.context)
                this.adapter = EditPlanSinglePlanAdapter().apply {
                    this.submitList(data)
                }
            }
        }
    }
}