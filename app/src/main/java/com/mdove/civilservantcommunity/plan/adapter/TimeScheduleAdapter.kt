package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansParams
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansStatus
import com.mdove.civilservantcommunity.plan.view.OnTimeScheduleAdapterListener

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleAdapter(val listener: OnTimeScheduleAdapterListener) :
    ListAdapter<TimeSchedulePlansParams, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<TimeSchedulePlansParams>() {
        override fun areItemsTheSame(
            oldItem: TimeSchedulePlansParams,
            newItem: TimeSchedulePlansParams
        ): Boolean {
            return oldItem.data.moduleId == newItem.data.moduleId
                    && oldItem.data.part == newItem.data.part
        }

        override fun areContentsTheSame(
            oldItem: TimeSchedulePlansParams,
            newItem: TimeSchedulePlansParams
        ): Boolean {
            return oldItem.data.content == newItem.data.content && oldItem.status == newItem.status
        }

        override fun getChangePayload(
            oldItem: TimeSchedulePlansParams,
            newItem: TimeSchedulePlansParams
        ): Any? {
            return if (oldItem.status != newItem.status) {
                PAYLOAD_STATUS
            } else null
        }

    }) {
    companion object {
        val PAYLOAD_STATUS = Any()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TimeScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_time_schedule_plans,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.contains(PAYLOAD_STATUS)) {
            (holder as? TimeScheduleViewHolder)?.payloadStatus(getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TimeScheduleViewHolder)?.let {
            it.bind(getItem(position))
        }
    }

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