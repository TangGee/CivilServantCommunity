package com.mdove.civilservantcommunity.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.civilservantcommunity.feed.bean.BaseFeedResp
import com.mdove.civilservantcommunity.feed.bean.FeedArticleResp
import com.mdove.civilservantcommunity.feed.bean.FeedPunchResp

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedAdapter(val listener: OnMainFeedClickListener? = null) :
        ListAdapter<BaseFeedResp, RecyclerView.ViewHolder>(object :
                DiffUtil.ItemCallback<BaseFeedResp>() {
            override fun areItemsTheSame(
                    oldItem: BaseFeedResp,
                    newItem: BaseFeedResp
            ): Boolean {
                if ((oldItem as? FeedArticleResp)?.article?.aid == (newItem as? FeedArticleResp)?.article?.aid) {
                    return true
                }
                if ((oldItem is FeedPunchResp) && (newItem is FeedPunchResp)) {
                    return true
                }
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                    oldItem: BaseFeedResp,
                    newItem: BaseFeedResp
            ): Boolean {
                if ((oldItem as? FeedArticleResp)?.article?.title == (newItem as? FeedArticleResp)?.article?.title) {
                    return true
                }
                if ((oldItem is FeedPunchResp) && (newItem is FeedPunchResp)) {
                    return true
                }
                return true
            }
        }) {
    private val TYPE_TOP_ONE = 1
    private val TYPE_TOP_TWO = 2
    private val TYPE_NORMAL = 3
    private val TYPE_FEED_PUNCH = 0
    private val TYPE_FEED_UGC = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FEED_PUNCH ->
                FeedPunchViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_feed_punch,
                                parent,
                                false
                        )
                )
            TYPE_FEED_UGC ->
                FeedPunchViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_feed_ugc,
                                parent,
                                false
                        )
                )
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
            0 -> TYPE_FEED_PUNCH
            1 -> TYPE_FEED_UGC
            2 -> TYPE_TOP_ONE
            3 -> TYPE_TOP_TWO
            else -> TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopOneViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
            is TopTwoViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
            is NormalViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
        }
    }

    inner class FeedPunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class FeedUGCViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TopOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ArticleResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }

    inner class TopTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ArticleResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ArticleResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }
}

interface OnMainFeedClickListener {
    fun onClick(resp: ArticleResp)
}