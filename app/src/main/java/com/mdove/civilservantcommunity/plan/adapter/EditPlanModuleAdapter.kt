package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.*
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.adapter.TYPE_NORMAL_ERROR_ICON
import com.mdove.civilservantcommunity.base.adapter.TYPE_NORMAL_ERROR_TITLE
import com.mdove.civilservantcommunity.base.adapter.createNormalErrorIconViewHolder
import com.mdove.civilservantcommunity.base.adapter.createNormalErrorTitleViewHolder
import com.mdove.civilservantcommunity.plan.model.PlanModuleBean
import com.mdove.civilservantcommunity.plan.model.PlanModuleStatus
import com.mdove.civilservantcommunity.plan.model.PlanModuleType

class EditPlanModuleAdapter(
    private val listener: OnPlanModuleClickListener? = null,
    private val singlePlanListener: OnSinglePlanClickListener? = null
) :
    ListAdapter<PlanModuleBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<PlanModuleBean>() {
        override fun areContentsTheSame(
            oldItem: PlanModuleBean,
            newItem: PlanModuleBean
        ): Boolean {
            return oldItem.moduleId == newItem.moduleId
                    && oldItem.beanSingles.size == newItem.beanSingles.size
                    && oldItem.moduleStatus == newItem.moduleStatus
                    && oldItem.changeVersion == newItem.changeVersion
        }

        override fun areItemsTheSame(
            oldItem: PlanModuleBean,
            newItem: PlanModuleBean
        ): Boolean {
            return oldItem.moduleId == newItem.moduleId
        }

        override fun getChangePayload(oldItem: PlanModuleBean, newItem: PlanModuleBean): Any? {
            return if (oldItem.beanSingles.size != newItem.beanSingles.size
                || oldItem.moduleStatus != newItem.moduleStatus
                || oldItem.changeVersion != newItem.changeVersion
            ) {
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
            getItem(position).moduleType == PlanModuleType.BTN_TIME_SCHEDULE -> 3
            getItem(position).moduleType == PlanModuleType.EDIT_PLANS_TIPS -> 4
            getItem(position).moduleType == PlanModuleType.CREATE_PLANS_TIPS -> 5
            getItem(position).moduleType == PlanModuleType.SCORE -> 6
            getItem(position).moduleType == PlanModuleType.ERROR_TITLE -> TYPE_NORMAL_ERROR_TITLE
            getItem(position).moduleType == PlanModuleType.ERROR_ICON -> TYPE_NORMAL_ERROR_ICON
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
            3 -> PlanModuleTimeScheduleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_btn_time_schedule,
                    parent,
                    false
                )
            )
            4 -> EditPlansTipsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_edit_plans_tips,
                    parent,
                    false
                )
            )
            5 -> CreatePlansTipsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_create_plans_tips,
                    parent,
                    false
                )
            )
            6 -> ScoreViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_plan_score,
                    parent,
                    false
                )
            )
            TYPE_NORMAL_ERROR_TITLE ->{
                parent.createNormalErrorTitleViewHolder(parent.context.getString(R.string.req_edit_plan_error))
            }
            TYPE_NORMAL_ERROR_ICON->{
                parent.createNormalErrorIconViewHolder()
            }
            else -> PlanModuleOkViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plan_module_btn_ok,
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
                listener?.onClickCreatePlans()
            }
        }
    }

    inner class PlanModuleTimeScheduleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener?.onClickTimeSchedule()
            }
        }
    }

    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class CreatePlansTipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class EditPlansTipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class PlanModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rlv = itemView.findViewById<RecyclerView>(R.id.rlv)
        private val btnClose = itemView.findViewById<AppCompatImageView>(R.id.btn_close)

        fun bind(data: PlanModuleBean) {
            itemView.findViewById<TextView>(R.id.tv_module_name).text =
                data.beanSingles[0].beanSingle.moduleName
            updateStatus(data)

            rlv.layoutManager = LinearLayoutManager(rlv.context)
            rlv.adapter = EditSinglePlanAdapter(singlePlanListener).apply {
                submitList(data.beanSingles)
            }
        }

        fun payload(data: PlanModuleBean) {
            updateStatus(data)
        }

        private fun updateStatus(data: PlanModuleBean) {
            if (data.moduleStatus == PlanModuleStatus.DELETE) {
                btnClose.setImageResource(R.drawable.vector_bg_delete_restore)
                rlv.visibility = View.GONE
            } else {
                btnClose.setImageResource(R.drawable.vector_bg_delete)
                rlv.visibility = View.VISIBLE
                (rlv.adapter as? EditSinglePlanAdapter)?.submitList(data.beanSingles)
            }
            btnClose.setOnClickListener {
                listener?.onDeletePlanModuleClick(data, data.moduleStatus != PlanModuleStatus.DELETE)
            }
        }
    }
}

interface OnPlanModuleClickListener {
    fun onDeletePlanModuleClick(data: PlanModuleBean, delete: Boolean)
    fun onClickCreatePlans()
    fun onClickTimeSchedule()
}