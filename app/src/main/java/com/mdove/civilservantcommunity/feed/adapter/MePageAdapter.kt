package com.mdove.civilservantcommunity.feed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.bean.FeedArticleFeedResp

/**
 * Created by MDove on 2019-09-11.
 */
class MePageAdapter(val listener: OnMePageClickListener? = null) :
    ListAdapter<FeedArticleFeedResp, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<FeedArticleFeedResp>() {
    override fun areItemsTheSame(
        oldItem: FeedArticleFeedResp,
        newItem: FeedArticleFeedResp
    ): Boolean {
        return oldItem.article.aid == newItem.article.aid
    }

    override fun areContentsTheSame(
        oldItem: FeedArticleFeedResp,
        newItem: FeedArticleFeedResp
    ): Boolean {
        return oldItem.article.title == newItem.article.title
    }
}) {
    private val TYPE_TOP_ONE = 1
    private val TYPE_TOP_TWO = 2
    private val TYPE_NORMAL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TOP_ONE -> FeedMeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_me_page_normal,
                    parent,
                    false
                )
            )
            TYPE_TOP_TWO -> FeedMeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_me_page_normal,
                    parent,
                    false
                )
            )
            else -> FeedMeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_me_page_normal,
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
            is FeedMeViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class TopOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedArticleFeedResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.makeTime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.article.content
        }
    }

    inner class FeedMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedArticleFeedResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.makeTime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.article.content
        }
    }

    inner class TopTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedArticleFeedResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.makeTime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.article.content
        }
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedArticleFeedResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.makeTime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.article.content
        }
    }
}

interface OnMePageClickListener{
    fun onClick(respFeed: FeedArticleFeedResp)
}