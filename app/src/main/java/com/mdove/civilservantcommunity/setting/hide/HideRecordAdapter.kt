package com.mdove.civilservantcommunity.setting.hide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.adapter.TYPE_NORMAL_TITLE
import com.mdove.civilservantcommunity.base.adapter.createNormalTitleViewHolder
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_DEV
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter.Companion.TYPE_FEED_QUICK_BTNS
import com.mdove.civilservantcommunity.setting.hide.model.HideRecordBean
import com.mdove.civilservantcommunity.setting.hide.model.TYPE_HIDE_RECORD_NO_CONTENT

/**
 * Created by MDove on 2020-01-06.
 */
class HideRecordAdapter(val list: List<HideRecordBean>, val listener: OnShowClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        val bean = list[position]
        return when {
            bean.type == TYPE_FEED_DEV -> TYPE_FEED_DEV
            bean.type == TYPE_FEED_QUICK_BTNS -> TYPE_FEED_QUICK_BTNS
            bean.type == TYPE_HIDE_RECORD_NO_CONTENT -> TYPE_HIDE_RECORD_NO_CONTENT
            else -> TYPE_NORMAL_TITLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_FEED_DEV->
                FeedDevTitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_dev_layout,
                        parent,
                        false
                    )
                )

            TYPE_FEED_QUICK_BTNS ->
                FeedQuickBtnsViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_quick_btns,
                        parent,
                        false
                    )
                )
            else-> parent.createNormalTitleViewHolder("没有任何隐藏的功能！")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    inner class FeedDevTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TextView>(R.id.tv_hide).text = "展示"
            itemView.findViewById<AppCompatImageView>(R.id.ic_hide)
                .setImageResource(R.drawable.vector_ic_show_feed_item)
            itemView.findViewById<LinearLayout>(R.id.btn_hide).setOnClickListener {
                listener.onClick(TYPE_FEED_DEV)
            }
        }
    }

    inner class FeedQuickBtnsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TextView>(R.id.tv_hide).text = "展示"
            itemView.findViewById<AppCompatImageView>(R.id.ic_hide)
                .setImageResource(R.drawable.vector_ic_show_feed_item)

            itemView.findViewById<LinearLayout>(R.id.btn_hide).setOnClickListener {
                listener.onClick(TYPE_FEED_QUICK_BTNS)
            }
        }
    }
}

interface OnShowClickListener {
    fun onClick(type: Int)
}