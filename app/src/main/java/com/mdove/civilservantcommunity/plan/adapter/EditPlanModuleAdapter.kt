package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import com.mdove.civilservantcommunity.plan.PlanModuleType
import com.mdove.civilservantcommunity.plan.SinglePlanBeanWrapper

class EditPlanModuleAdapter(
    private val listener: OnPlanModuleClickListener,
    private val singlePlanListener: OnSinglePlanClickListener
) :
    ListAdapter<PlanModuleBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<PlanModuleBean>() {
        override fun areContentsTheSame(
            oldItem: PlanModuleBean,
            newItem: PlanModuleBean
        ): Boolean {
            return oldItem.moduleId == newItem.moduleId
                    && oldItem.beanSingles.size == newItem.beanSingles.size
        }

        override fun areItemsTheSame(
            oldItem: PlanModuleBean,
            newItem: PlanModuleBean
        ): Boolean {
            return oldItem.moduleId == newItem.moduleId
        }

        override fun getChangePayload(oldItem: PlanModuleBean, newItem: PlanModuleBean): Any? {
            return if (oldItem.beanSingles.size != newItem.beanSingles.size) {
                PAYLOAD_SINGLE_PLANS
            } else {
                null
            }
        }
    }) {

    companion object {
        val PAYLOAD_SINGLE_PLANS = Any()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position).moduleType == PlanModuleType.PADDING -> 2
            getItem(position).moduleType == PlanModuleType.BTN_OK -> 1
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> PlanModuleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_plan_module,
                    parent,
                    false
                )
            )
            2 -> PaddingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_rlv_padding,
                    parent,
                    false
                )
            )
            else -> PlanModuleOkViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_ok,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.contains(PAYLOAD_SINGLE_PLANS)) {
            (holder as? PlanModuleViewHolder)?.let {
                it.payload(getItem(position))
            } ?: also {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
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
                listener.onClickCreatePlans()
            }
        }
    }

    inner class PaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class PlanModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rlv = itemView.findViewById<RecyclerView>(R.id.rlv)
        fun bind(data: PlanModuleBean) {
            itemView.findViewById<TextView>(R.id.tv_module_name).text =
                data.beanSingles[0].beanSingle.moduleName
            itemView.findViewById<AppCompatImageView>(R.id.btn_close).setOnClickListener {
                listener.onDeletePlanModuleClick(data)
            }

            rlv.layoutManager = LinearLayoutManager(rlv.context)
            rlv.adapter = EditSinglePlanAdapter(singlePlanListener).apply {
                submitList(data.beanSingles)
            }
        }

        fun payload(data: PlanModuleBean) {
            rlv.layoutManager = LinearLayoutManager(rlv.context)
            val adapter =
                (rlv.adapter as? EditSinglePlanAdapter) ?: EditSinglePlanAdapter(singlePlanListener)
            rlv.adapter = adapter
            adapter.submitList(data.beanSingles)
        }
    }
}

interface OnPlanModuleClickListener {
    fun onDeletePlanModuleClick(data: PlanModuleBean)
    fun onClickCreatePlans()
}