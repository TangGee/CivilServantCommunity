package com.mdove.civilservantcommunity.feed.adapter

import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.plan.SinglePlanStatus
import com.mdove.civilservantcommunity.plan.SinglePlanType
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.view.timeline.TimeLineView

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedAdapter(
    val listener: OnMainFeedClickListener? = null,
    val checkListener: OnMainFeedTodayPlanCheckListener? = null
) :
    ListAdapter<BaseFeedResp, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<BaseFeedResp>() {
        override fun areItemsTheSame(
            oldItem: BaseFeedResp,
            newItem: BaseFeedResp
        ): Boolean {
            if ((oldItem is FeedArticleResp) && (newItem is FeedArticleResp)) {
                return true
            }
            if ((oldItem is FeedTimeLineFeedTodayPlansTitleResp) && (newItem is FeedTimeLineFeedTodayPlansTitleResp)) {
                return true
            }
            if ((oldItem is FeedQuickBtnsResp) && (newItem is FeedQuickBtnsResp)) {
                return true
            }
            if ((oldItem is FeedPunchResp) && (newItem is FeedPunchResp)) {
                return true
            }
            if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                return true
            }
            if ((oldItem is FeedPaddingStub) && (newItem is FeedPaddingStub)) {
                return true
            }
            if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                return true
            }
            if ((oldItem is FeedTimeLineFeedTodayPlansResp) && (newItem is FeedTimeLineFeedTodayPlansResp)) {
                return true
            }
            if ((oldItem is FeedTimeLineFeedTitleResp) && (newItem is FeedTimeLineFeedTitleResp)) {
                return true
            }
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: BaseFeedResp,
            newItem: BaseFeedResp
        ): Boolean {
            return if ((oldItem is FeedArticleResp) && (newItem is FeedArticleResp)) {
                oldItem.article.title == newItem.article.title
            } else if ((oldItem is FeedPunchResp) && (newItem is FeedPunchResp)) {
                oldItem.count == newItem.count
            } else if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                oldItem.params == newItem.params
            } else if ((oldItem is FeedTimeLineFeedTodayPlansResp) && (newItem is FeedTimeLineFeedTodayPlansResp)) {
                oldItem.params.statusSingle == newItem.params.statusSingle
            } else if ((oldItem is FeedTimeLineFeedTodayPlansTitleResp) && (newItem is FeedTimeLineFeedTodayPlansTitleResp)) {
                return true
            } else if ((oldItem is FeedTimeLineFeedTitleResp) && (newItem is FeedTimeLineFeedTitleResp)) {
                return true
            } else {
                true
            }
        }

        override fun getChangePayload(oldItem: BaseFeedResp, newItem: BaseFeedResp): Any? {
            return when {
                (oldItem as? FeedPunchResp)?.count != (newItem as? FeedPunchResp)?.count -> PAYLOAD_PUNCH
                (oldItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle != (newItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle -> PAYLOAD_TODAY_PLANS
                else -> null
            }
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
        const val TYPE_FEED_TIME_LINE_FEED_TITLE = 9
        const val TYPE_FEED_TIME_LINE_FEED_TODAY_PLAM = 10
        const val TYPE_FEED_TIME_LINE_FEED_TODAY_PLAM_TITLE = 11
        const val TYPE_FEED_PADDING = 12

        const val CLICK_QUICK_BTN_PLAN = 101
        const val CLICK_QUICK_BTN_PUNCH = 102
        const val CLICK_QUICK_BTN_UGC = 103
        const val CLICK_QUICK_BTN_ME = 104
        const val CLICK_FEED_TIME_LINE_PLAN = 105

        val PAYLOAD_PUNCH = Any()
        val PAYLOAD_TODAY_PLANS = Any()
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
            TYPE_FEED_TIME_LINE_FEED_TODAY_PLAM ->
                FeedTimeLineFeedTodayPlansViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_today_plans,
                        parent,
                        false
                    )
                )
            TYPE_FEED_TIME_LINE_FEED_TODAY_PLAM_TITLE ->
                FeedTimeLineFeedTodayPlansTitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_today_plans_title,
                        parent,
                        false
                    )
                )
            TYPE_FEED_TIME_LINE_FEED_TITLE ->
                FeedTimeLineFeedTitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_feed_main_time_line_title,
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
                FeedDateViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_date,
                        parent,
                        false
                    )
                )
            TYPE_FEED_PADDING ->
                FeedPaddingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_padding,
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
            is FeedPaddingStub -> TYPE_FEED_PADDING
            is FeedTimeLineFeedTitleResp -> TYPE_FEED_TIME_LINE_FEED_TITLE
            is FeedTimeLineFeedTodayPlansResp -> TYPE_FEED_TIME_LINE_FEED_TODAY_PLAM
            is FeedTimeLineFeedTodayPlansTitleResp -> TYPE_FEED_TIME_LINE_FEED_TODAY_PLAM_TITLE
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
        } else if (payloads.contains(PAYLOAD_TODAY_PLANS)) {
            if (holder is FeedTimeLineFeedTodayPlansViewHolder) {
                holder.payloadBind(getItem(position) as FeedTimeLineFeedTodayPlansResp)
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
            is FeedDateViewHolder -> holder.bind()
            is NewStyleViewHolder -> {
                (getItem(position) as? FeedArticleResp)?.let {
                    holder.bind(it)
                }
            }
            is FeedTimeLineFeedTodayPlansViewHolder -> holder.bind((getItem(position) as FeedTimeLineFeedTodayPlansResp))
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

    inner class FeedUGCViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(TYPE_FEED_UGC, null)
                }
            }
        }
    }

    inner class FeedDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay = itemView.findViewById<TextView>(R.id.tv_day)
        private val tvMonth = itemView.findViewById<TextView>(R.id.tv_month)
        private val tvWeek = itemView.findViewById<TextView>(R.id.tv_week)

        fun bind() {
            val time = System.currentTimeMillis()
            tvMonth.text = "${TimeUtils.getMonth(time)}月"
            tvDay.text = "${TimeUtils.getDay(time)}日"
            tvWeek.text = TimeUtils.getDayOfWeek(time)
        }
    }

    inner class FeedPaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class FeedTimeLineFeedTodayPlansViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.text)
        private val tvSucTime = itemView.findViewById<TextView>(R.id.tv_create_time)
        private val timeLine = itemView.findViewById<TimeLineView>(R.id.time_line)
        private val tvModule = itemView.findViewById<TextView>(R.id.tv_module)
        private val cb = itemView.findViewById<AppCompatCheckBox>(R.id.cb_today_plan)

        fun bind(resp: FeedTimeLineFeedTodayPlansResp) {
            reset()
            title.text = resp.params.beanSingle.content
            tvModule.text = resp.params.beanSingle.moduleName
            resp.sucTime?.let {
                tvSucTime.visibility = View.VISIBLE
                tvSucTime.text = TimeUtils.getDateChinese(it)
            } ?: also {
                tvSucTime.visibility = View.GONE
            }

            bindSelect(resp.params.statusSingle == SinglePlanStatus.SELECT)
            cb.isChecked = resp.params.statusSingle == SinglePlanStatus.SELECT
            cb.setOnCheckedChangeListener { _, isChecked ->
                checkListener?.onCheck(resp, isChecked)
            }
            if (resp.params.typeSingle == SinglePlanType.LAST_PLAN) {
                timeLine.hideBottomLine()
            }
        }

        fun payloadBind(resp: FeedTimeLineFeedTodayPlansResp) {
            bindSelect(resp.params.statusSingle == SinglePlanStatus.SELECT)
        }

        private fun reset() {
            cb.setOnCheckedChangeListener(null)
        }

        private fun bindSelect(select: Boolean) {
            if (select) {
                title.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
                title.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
                tvModule.setBackgroundResource(R.drawable.bg_round_grey)
                tvModule.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
                tvModule.setBackgroundResource(R.drawable.bg_round_grey)
            } else {
                title.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                title.paint.flags = 0
                title.paint.flags = ANTI_ALIAS_FLAG
                tvModule.setBackgroundResource(R.drawable.bg_round_blue)
                tvModule.paint.flags = 0
                tvModule.paint.flags = ANTI_ALIAS_FLAG
            }
        }
    }

    inner class FeedTimeLineFeedTodayPlansTitleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    inner class FeedTimeLineFeedTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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
        private val timeLine = itemView.findViewById<TimeLineView>(R.id.time_line)
        fun bind(data: FeedArticleResp) {
            reset()
            listener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onClick(TYPE_TOP_ONE, data.article)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.maketime?.let {
                TimeUtils.getDateChinese(java.lang.Long.getLong(it))
            } ?: ""
            itemView.findViewById<TextView>(R.id.tv_content).text = data.article.content
            if (data.hideEndLine) {
                timeLine.hideBottomLine()
            }
        }

        private fun reset() {
            timeLine.showBottomLine()
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

interface OnMainFeedTodayPlanCheckListener {
    fun onCheck(resp: FeedTimeLineFeedTodayPlansResp, isCheck: Boolean)
}