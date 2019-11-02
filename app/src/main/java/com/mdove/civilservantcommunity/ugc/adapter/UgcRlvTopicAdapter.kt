package com.mdove.civilservantcommunity.ugc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.adapter.createWidthPadding
import com.mdove.civilservantcommunity.ugc.bean.UGCRlvTopicBean

/**
 * Created by MDove on 2019-11-02.
 */
class UgcRlvTopicAdapter(private val listener: OnTopicSelectListener) :
    ListAdapter<UGCRlvTopicBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<UGCRlvTopicBean>() {
        override fun areItemsTheSame(oldItem: UGCRlvTopicBean, newItem: UGCRlvTopicBean): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: UGCRlvTopicBean,
            newItem: UGCRlvTopicBean
        ): Boolean {
            return oldItem.selectStatus == newItem.selectStatus
        }

        override fun getChangePayload(oldItem: UGCRlvTopicBean, newItem: UGCRlvTopicBean): Any? {
            return if (oldItem.selectStatus != newItem.selectStatus) {
                PAYLOAD_TOPIC
            } else {
                null
            }
        }

    }) {
    companion object {
        val PAYLOAD_TOPIC = Any()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> parent.createWidthPadding(8F)
            else -> TopicViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_ugc_rlv_topic,
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
        if (payloads.contains(PAYLOAD_TOPIC)) {
            (holder as? TopicViewHolder)?.payload(getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TopicViewHolder)?.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            0 -> 0
            else -> 1
        }
    }

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layout = itemView.findViewById<ConstraintLayout>(R.id.layout)
        private val title = itemView.findViewById<TextView>(R.id.tv_topic)
        fun bind(data: UGCRlvTopicBean) {
            title.text = data.name
            layout.setOnClickListener {
                listener.onSelect(data, !data.selectStatus)
            }
            selectStatus(data.selectStatus)
        }

        fun payload(data: UGCRlvTopicBean) {
            selectStatus(data.selectStatus)
        }

        private fun selectStatus(select: Boolean) {
            layout.setBackgroundResource(if (select) R.drawable.bg_rlv_topic_select else R.drawable.bg_rlv_topic_unselect)
        }
    }
}

interface OnTopicSelectListener {
    fun onSelect(bean: UGCRlvTopicBean, select: Boolean)
}