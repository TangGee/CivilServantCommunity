package com.mdove.civilservantcommunity.feed.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import android.text.Html
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.dependent.common.recyclerview.timelineitemdecoration.itemdecoration.DotItemDecoration
import com.mdove.dependent.common.utils.TimeUtils


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
            if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
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
                return oldItem.count == newItem.count
            }
            if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                return oldItem.params == newItem.params
            }
            return true
        }

        override fun getChangePayload(oldItem: BaseFeedResp, newItem: BaseFeedResp): Any? {
            return if ((oldItem as? FeedPunchResp)?.count != (newItem as? FeedPunchResp)?.count) {
                return PAYLOAD_PUNCH
            } else null
        }
    }) {

    companion object {
        const val TYPE_TOP_ONE = 1
        const val TYPE_TOP_TWO = 2
        const val TYPE_NORMAL = 3
        const val TYPE_FEED_PUNCH = 0
        const val TYPE_FEED_PLAN = 5
        const val TYPE_FEED_UGC = 4
        const val TYPE_FEED_TODAY_PLAN = 6
        const val TYPE_FEED_QUICK_BTNS = 7
        const val TYPE_FEED_DATE = 8

        const val CLICK_QUICK_BTN_PLAN = 101
        const val CLICK_QUICK_BTN_PUNCH = 102
        const val CLICK_QUICK_BTN_UGC = 103
        const val CLICK_QUICK_BTN_ME = 104


        val PAYLOAD_PUNCH = Any()
    }

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
            TYPE_FEED_TODAY_PLAN ->
                FeedTodayPlanViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_today_plan,
                        parent,
                        false
                    )
                )
            TYPE_FEED_UGC ->
                FeedUGCViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_ugc,
                        parent,
                        false
                    )
                )
            TYPE_FEED_DATE ->
                FeedUGCViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_date,
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
            TYPE_FEED_PLAN ->
                FeedPlanViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_day_plan,
                        parent,
                        false
                    )
                )
            TYPE_TOP_ONE ->
                NewStyleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_normal_new,
                        parent,
                        false
                    )
                )
            TYPE_TOP_TWO ->
                NewStyleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_normal_new,
                        parent,
                        false
                    )
                )
            else ->
                NewStyleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_normal_new,
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedPunchResp -> TYPE_FEED_PUNCH
            is FeedUGCResp -> TYPE_FEED_UGC
            is FeedPlanResp -> TYPE_FEED_PLAN
            is FeedTodayPlanResp -> TYPE_FEED_TODAY_PLAN
            is FeedQuickBtnsResp -> TYPE_FEED_QUICK_BTNS
            is FeedDateResp -> TYPE_FEED_DATE
            else -> TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.contains(PAYLOAD_PUNCH)) {
            if (holder is FeedPunchViewHolder) {
                holder.payloadBind(getItem(position) as FeedPunchResp)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeedPunchViewHolder -> holder.bind((getItem(position) as FeedPunchResp))
            is TopOneViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
            is TopTwoViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
            is NormalViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
            is NewStyleViewHolder -> holder.bind((getItem(position) as FeedArticleResp).article)
            is FeedTodayPlanViewHolder -> holder.bind((getItem(position) as FeedTodayPlanResp))
        }
    }

    inner class FeedPunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(params: FeedPunchResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    //                    if (!params.hasPunch) {
                    listener.onClick(TYPE_FEED_PUNCH, null)
//                    }
                }
            }
            val content = itemView.context.getString(R.string.string_punch_title_sub, params.count)
            itemView.findViewById<TextView>(R.id.tv_title_sub).text = Html.fromHtml(content)
            itemView.findViewById<TextView>(R.id.btn_punch).text =
                if (params.hasPunch) "今天已打卡" else "打卡"
        }

        fun payloadBind(params: FeedPunchResp) {
            val content = itemView.context.getString(R.string.string_punch_title_sub, params.count)
            itemView.findViewById<TextView>(R.id.tv_title_sub).text = Html.fromHtml(content)
            itemView.findViewById<TextView>(R.id.btn_punch).text =
                if (params.hasPunch) "今天已打卡" else "打卡"
        }
    }

    inner class FeedTodayPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rlv = itemView.findViewById<RecyclerView>(R.id.rlv_today)

        init {
            rlv.layoutManager = LinearLayoutManager(itemView.context)
            val mItemDecoration = DotItemDecoration.Builder(itemView.context)
                .setOrientation(DotItemDecoration.VERTICAL)
                .setItemStyle(DotItemDecoration.STYLE_DRAW)
                .setTopDistance(20f)//dp
                .setItemInterVal(10f)//dp
                .setItemPaddingLeft(30f)//default value equals to item interval value
                .setItemPaddingRight(20f)//default value equals to item interval value
                .setDotColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
                .setDotRadius(5)//dp
                .setDotPaddingTop(0)
                .setDotInItemOrientationCenter(false)//set true if you want the dot align center
                .setLineColor(ContextCompat.getColor(itemView.context, R.color.grey_200))
                .setLineWidth(3f)//dp
                .setOnlyLeftMarginLeft(40F)
                .setEndText("END")
                .setOnlyLeft(true)
                .setTextColor(Color.WHITE)
                .setTextSize(16f)//sp
                .setDotPaddingText(2f)//dp.The distance between the last dot and the end text
                .setBottomDistance(40f)//you can add a distance to make bottom line longer
                .create()
            rlv.addItemDecoration(mItemDecoration)
        }

        fun bind(resp: FeedTodayPlanResp) {
            resp.params?.let {
                rlv.adapter = FeedPlanTodayItemsAdapter(
                    itemView.context,
                    it
                )
            }
        }
    }

    inner class FeedUGCViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(TYPE_FEED_UGC, null)
                }
            }
        }
    }

    inner class FeedDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class FeedQuickBtnsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            listener?.let { listener ->
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_plan).setOnClickListener {
                    listener.onClick(CLICK_QUICK_BTN_PLAN, null)
                }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_punch).setOnClickListener {
                    listener.onClick(CLICK_QUICK_BTN_PUNCH, null)
                }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_ugc).setOnClickListener {
                    listener.onClick(CLICK_QUICK_BTN_UGC, null)
                }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_me).setOnClickListener {
                    listener.onClick(CLICK_QUICK_BTN_ME, null)
                }
            }
        }
    }

    inner class FeedPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(TYPE_FEED_PLAN, null)
                }
            }
        }
    }

    inner class NewStyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ArticleResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(TYPE_TOP_ONE, data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime?.let {
                TimeUtils.getDateChinese(java.lang.Long.getLong(it))
            } ?: ""
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }

    inner class TopOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ArticleResp) {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(TYPE_TOP_ONE, data)
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
                    listener.onClick(TYPE_TOP_TWO, data)
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
                    listener.onClick(TYPE_NORMAL, data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.maketime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.content
        }
    }
}

interface OnMainFeedClickListener {
    fun onClick(type: Int, resp: ArticleResp?)
}