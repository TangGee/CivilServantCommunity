package com.mdove.civilservantcommunity.plan.adapter

import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.view.OnTimeScheduleAdapterListener
import android.text.SpannableString
import com.mdove.civilservantcommunity.plan.model.*


/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleAdapter(val listener: OnTimeScheduleAdapterListener) :
    ListAdapter<TimeScheduleBaseParams, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<TimeScheduleBaseParams>() {
        override fun areItemsTheSame(
            oldItem: TimeScheduleBaseParams,
            newItem: TimeScheduleBaseParams
        ): Boolean {
            return (oldItem as? TimeSchedulePlansParams)?.data?.moduleId == (newItem as? TimeSchedulePlansParams)?.data?.moduleId
                    && (oldItem as? TimeSchedulePlansParams)?.data?.part == (newItem as? TimeSchedulePlansParams)?.data?.part
        }

        override fun areContentsTheSame(
            oldItem: TimeScheduleBaseParams,
            newItem: TimeScheduleBaseParams
        ): Boolean {
            return (oldItem as? TimeSchedulePlansParams)?.data?.content == (oldItem as? TimeSchedulePlansParams)?.data?.content && oldItem.status == newItem.status
        }

        override fun getChangePayload(
            oldItem: TimeScheduleBaseParams,
            newItem: TimeScheduleBaseParams
        ): Any? {
            return if (oldItem.status != newItem.status) {
                PAYLOAD_STATUS
            } else null
        }

    }) {
    companion object {
        val PAYLOAD_STATUS = Any()

        const val TYPE_NORMAL = 1
        const val TYPE_EMPTY = 2
        const val TYPE_NO_PLAN = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TimeSchedulePlansParams -> TYPE_NORMAL
            is TimeScheduleEmptyParams -> TYPE_EMPTY
            is TimeScheduleNoPlanParams -> TYPE_NO_PLAN
            else -> TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_EMPTY) {
            TimeScheduleEmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_time_schedule_plans_empty,
                    parent,
                    false
                )
            )
        }else if (viewType == TYPE_NO_PLAN) {
            TimeScheduleNoPlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_time_schedule_plans_no_plan,
                    parent,
                    false
                )
            )
        } else {
            TimeScheduleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_time_schedule_plans,
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
        if (payloads.contains(PAYLOAD_STATUS)) {
            (holder as? TimeScheduleViewHolder)?.payloadStatus(getItem(position) as TimeSchedulePlansParams)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TimeScheduleViewHolder)?.let {
            it.bind(getItem(position) as TimeSchedulePlansParams)
        }
    }

    inner class TimeScheduleEmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TextView>(R.id.tv_title).apply {
                val str = "没有任何计划，是否立刻创建计划~~"
                text = SpannableString(str).apply {
                    setSpan(
                        UnderlineSpan(),
                        7,
                        str.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                setOnClickListener {
                    listener.onClickGotoCreatePlans()
                }
            }
        }
    }

    inner class TimeScheduleNoPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TimeScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)

        fun bind(params: TimeSchedulePlansParams) {
            tvTitle.text = params.data.content
            tvTitle.setOnLongClickListener {
                listener.onLongClick(params.data, it)
                true
            }
        }

        fun payloadStatus(params: TimeSchedulePlansParams) {
            if (params.status == TimeSchedulePlansStatus.GONE) {
                itemView.alpha = 0.5F
            } else {
                itemView.alpha = 1F
            }
        }
    }
}