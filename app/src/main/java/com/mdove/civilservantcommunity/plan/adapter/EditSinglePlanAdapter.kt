package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.plan.SinglePlanBeanWrapper
import com.mdove.civilservantcommunity.plan.SinglePlanType

class EditSinglePlanAdapter(private val listener: OnSinglePlanClickListener? = null) :
    ListAdapter<SinglePlanBeanWrapper, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<SinglePlanBeanWrapper>() {
        override fun areContentsTheSame(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Boolean {
            return oldItem.beanSingle.content == oldItem.beanSingle.content
        }

        override fun areItemsTheSame(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Boolean {
            return oldItem.beanSingle.moduleId == newItem.beanSingle.moduleId
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
                    R.layout.item_edit_single_plan,
                    parent,
                    false
                )
            )
        } else {
            return ModulePlanCustomViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_single_custom_plan,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ModulePlanViewHolder -> {
                holder.bind(data = getItem(position).beanSingle)
            }
            is ModulePlanCustomViewHolder -> {
                holder.bind(data = getItem(position).beanSingle)
            }
        }
    }

    inner class ModulePlanCustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val et = itemView.findViewById<EditText>(R.id.et_custom_content)

        fun bind(data: SinglePlanBean) {
            val custom = et.text.toString()
            itemView.findViewById<AppCompatImageView>(R.id.btn_send).setOnClickListener {
                if (!custom.isBlank()) {
                    listener?.onCustomClick(data.copy(content = custom))
                }
            }
        }
    }

    inner class ModulePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: SinglePlanBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
            itemView.findViewById<AppCompatImageView>(R.id.btn_close).setOnClickListener {
                listener?.onDeleteSinglePlanClick(data)
            }
        }
    }
}

interface OnSinglePlanClickListener {
    fun onDeleteSinglePlanClick(data: SinglePlanBean)
    fun onCustomClick(data: SinglePlanBean)
}