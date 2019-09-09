package com.mdove.civilservantcommunity.feed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.bean.FeedDataResp

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedAdapter : ListAdapter<FeedDataResp, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<FeedDataResp>() {
    override fun areItemsTheSame(
        oldItem: FeedDataResp,
        newItem: FeedDataResp
    ): Boolean {
        return oldItem.aid == newItem.aid
    }

    override fun areContentsTheSame(
        oldItem: FeedDataResp,
        newItem: FeedDataResp
    ): Boolean {
        return oldItem.title == newItem.title
    }
}) {
    private val TYPE_TOP_ONE = 1
    private val TYPE_TOP_TWO = 2
    private val TYPE_NORMAL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TOP_ONE -> TopOneViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_top1,
                    parent,
                    false
                )
            )
            TYPE_TOP_TWO -> TopTwoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_top2,
                    parent,
                    false
                )
            )
            else -> NormalViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_normal,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_TOP_ONE
            1 -> TYPE_TOP_TWO
            else -> TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopOneViewHolder -> holder.bind(getItem(position))
            is TopTwoViewHolder -> holder.bind(getItem(position))
            is NormalViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class TopOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedDataResp) {
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }

    inner class TopTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedDataResp) {
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedDataResp) {
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }
}