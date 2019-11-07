package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansParams
import com.mdove.civilservantcommunity.plan.view.OnTimeScheduleAdapterListener

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleAdapter(val listener: OnTimeScheduleAdapterListener) :
    ListAdapter<FeedTimeLineFeedTodayPlansResp, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<FeedTimeLineFeedTodayPlansResp>() {
        override fun areItemsTheSame(
            oldItem: FeedTimeLineFeedTodayPlansResp,
            newItem: FeedTimeLineFeedTodayPlansResp
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FeedTimeLineFeedTodayPlansResp,
            newItem: FeedTimeLineFeedTodayPlansResp
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TimeScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_time_schedule_plans,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TimeScheduleViewHolder)?.let {
            it.bind(getItem(position))
        }
    }

    inner class TimeScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        fun bind(data: FeedTimeLineFeedTodayPlansResp) {
            tvTitle.text = data.params.beanSingle.content
            tvTitle.setOnLongClickListener {
                itemView.visibility = View.INVISIBLE
                listener.onLongClick(data.params.beanSingle, it)
                true
            }
        }
    }
}