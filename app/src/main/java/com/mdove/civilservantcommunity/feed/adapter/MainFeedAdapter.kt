package com.mdove.civilservantcommunity.feed.adapter

import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.model.SinglePlanStatus
import com.mdove.dependent.common.gson.GsonProvider
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener
import com.mdove.dependent.common.view.roundcorner.RoundCornerConstraintLayout
import com.mdove.dependent.common.view.timeline.TimeLineView

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedAdapter(
    val listener: OnMainFeedClickListener? = null,
    val normalListener: OnNormalFeedListener? = null,
    val questionListener: OnMainFeedQuickClickListener? = null,
    val checkListener: OnMainFeedTodayPlanCheckListener? = null,
    val clickListener: OnMainFeedHideClickListener? = null
) :
    ListAdapter<BaseFeedResp, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<BaseFeedResp>() {
        override fun areItemsTheSame(
            oldItem: BaseFeedResp,
            newItem: BaseFeedResp
        ): Boolean {
            if ((oldItem is FeedArticleFeedResp) && (newItem is FeedArticleFeedResp)) {
                return true
            }
            if ((oldItem is FeedQuestionFeedResp) && (newItem is FeedQuestionFeedResp)) {
                return true
            }
            if ((oldItem is FeedTimeLineFeedTodayPlansTitleResp) && (newItem is FeedTimeLineFeedTodayPlansTitleResp)) {
                return true
            }
            if ((oldItem is FeedTimeLineFeedTodayPlansTipsTitleResp) && (newItem is FeedTimeLineFeedTodayPlansTipsTitleResp)) {
                return true
            }
            if ((oldItem is FeedQuickBtnsResp) && (newItem is FeedQuickBtnsResp)) {
                return true
            }
            if ((oldItem is FeedLoadMoreResp) && (newItem is FeedLoadMoreResp)) {
                return true
            }
            if ((oldItem is FeedPunchResp) && (newItem is FeedPunchResp)) {
                return true
            }
            if ((oldItem is FeedEncourageTipsResp) && (newItem is FeedEncourageTipsResp)) {
                return true
            }
            if ((oldItem is FeedQuickEditNewPlanResp) && (newItem is FeedQuickEditNewPlanResp)) {
                return true
            }
            if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                return true
            }
            if ((oldItem is FeedDateResp) && (newItem is FeedDateResp)) {
                return oldItem.name == newItem.name
            }
            if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                return true
            }
            if ((oldItem is FeedNoContentResp) && (newItem is FeedNoContentResp)) {
                return true
            }
            if ((oldItem is FeedNetworkErrorTitleResp) && (newItem is FeedNetworkErrorTitleResp)) {
                return true
            }
            if ((oldItem is FeedTimeLineFeedTodayPlansResp) && (newItem is FeedTimeLineFeedTodayPlansResp)) {
                return true
            }
            if ((oldItem is FeedDevTitleResp) && (newItem is FeedDevTitleResp)) {
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
            return if ((oldItem is FeedTimeLineFeedTodayPlansTitleResp) && (newItem is FeedTimeLineFeedTodayPlansTitleResp)) {
                return true
            } else if ((oldItem is FeedTimeLineFeedTodayPlansTipsTitleResp) && (newItem is FeedTimeLineFeedTodayPlansTipsTitleResp)) {
                return true
            } else if ((oldItem is FeedTimeLineFeedTitleResp) && (newItem is FeedTimeLineFeedTitleResp)) {
                return true
            } else if ((oldItem is FeedQuickEditNewPlanResp) && (newItem is FeedQuickEditNewPlanResp)) {
                return true
            } else if ((oldItem is FeedQuestionFeedResp) && (newItem is FeedQuestionFeedResp)) {
                return oldItem.question.qid == newItem.question.qid
            } else if ((oldItem is FeedNetworkErrorTitleResp) && (newItem is FeedNetworkErrorTitleResp)) {
                return true
            } else if ((oldItem is FeedDateResp) && (newItem is FeedDateResp)) {
                return oldItem.isSameDay == newItem.isSameDay && newItem.name == oldItem.name
            } else if ((oldItem is FeedNoContentResp) && (newItem is FeedNoContentResp)) {
                return true
            } else if ((oldItem is FeedArticleFeedResp) && (newItem is FeedArticleFeedResp)) {
                oldItem.article.title == newItem.article.title
            } else if ((oldItem is FeedPunchResp) && (newItem is FeedPunchResp)) {
                oldItem.count == newItem.count
            } else if ((oldItem is FeedLoadMoreResp) && (newItem is FeedLoadMoreResp)) {
                return true
            } else if ((oldItem is FeedTodayPlanResp) && (newItem is FeedTodayPlanResp)) {
                oldItem.params == newItem.params
            } else if ((oldItem is FeedEncourageTipsResp) && (newItem is FeedEncourageTipsResp)) {
                return true
            } else if ((oldItem is FeedDevTitleResp) && (newItem is FeedDevTitleResp)) {
                return true
            } else if ((oldItem is FeedTimeLineFeedTodayPlansResp) && (newItem is FeedTimeLineFeedTodayPlansResp)) {
                oldItem.params.statusSingle == newItem.params.statusSingle && oldItem.params.beanSingle.content == newItem.params.beanSingle.content
                        && oldItem.hideEndLine == newItem.hideEndLine
            } else {
                true
            }
        }

        override fun getChangePayload(oldItem: BaseFeedResp, newItem: BaseFeedResp): Any? {
            return when {
                (oldItem as? FeedPunchResp)?.count != (newItem as? FeedPunchResp)?.count -> PAYLOAD_PUNCH
                (oldItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle != (newItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle -> PAYLOAD_TODAY_PLANS_STATUS
                (oldItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle != (newItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle && (newItem as? FeedTimeLineFeedTodayPlansResp)?.params?.statusSingle == SinglePlanStatus.CONTENT_CHANGE -> PAYLOAD_TODAY_PLANS_EDIT
                (oldItem as? FeedTimeLineFeedTodayPlansResp)?.params?.beanSingle?.content != (newItem as? FeedTimeLineFeedTodayPlansResp)?.params?.beanSingle?.content -> PAYLOAD_TODAY_PLANS_EDIT
                (oldItem as? FeedDateResp)?.isSameDay == (newItem as? FeedDateResp)?.isSameDay -> PAYLOAD_TIME_UPDATE
                (oldItem.hideEndLine != newItem.hideEndLine) -> PAYLOAD_HAS_END_LINE
                else -> null
            }
        }
    }) {

    companion object {
        const val TYPE_TOP_ONE = 1
        const val TYPE_FEED_NORMAL_CARD = 3
        const val TYPE_FEED_PUNCH = 0
        const val TYPE_FEED_TODAY_PLAN = 6
        const val TYPE_FEED_QUICK_BTNS = 7
        const val TYPE_FEED_DATE = 8
        const val TYPE_FEED_TIME_LINE_FEED_TITLE = 9
        const val TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN = 10
        const val TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_TITLE = 11
        const val TYPE_FEED_NETWORK_ERROR = 13
        const val TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS = 14
        const val TYPE_FEED_EDIT_NEW_PLAN = 15
        const val TYPE_FEED_DEV = 16
        const val TYPE_FEED_QUESTION_CARD = 17
        const val TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_APPLY_OLD = 18
        const val TYPE_FEED_QUESTION_CARD_CLICK_QUICK_SEND = 19
        const val TYPE_FEED_TODAY_PLANS_ENCOURAGE = 20
        const val TYPE_FEED_LOAD_MORE = 21
        const val TYPE_FEED_NO_CONTENT = 22
        const val TYPE_FEED_BOTTOM_PADDING = 23

        const val CLICK_QUICK_BTN_PLAN = 101
        const val CLICK_QUICK_BTN_PUNCH = 102
        const val CLICK_QUICK_BTN_UGC = 103
        const val CLICK_QUICK_BTN_ME = 104
        const val CLICK_QUICK_BTN_TIME_SCHEDULE = 106
        const val CLICK_QUICK_BTN_HISTORY_PLANS = 107
        const val CLICK_MAIN_FEED_LOGIN = 108

        val PAYLOAD_PUNCH = Any()
        val PAYLOAD_TODAY_PLANS_STATUS = Any()
        val PAYLOAD_TODAY_PLANS_EDIT = Any()
        val PAYLOAD_TIME_UPDATE = Any()
        val PAYLOAD_HAS_END_LINE = Any()
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
            TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN ->
                FeedTimeLineFeedTodayPlansViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_today_plans,
                        parent,
                        false
                    )
                )
            TYPE_FEED_EDIT_NEW_PLAN ->
                FeedEditNewPlanViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_send_new_plan,
                        parent,
                        false
                    )
                )
            TYPE_FEED_QUESTION_CARD ->
                FeedQuestionViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_question_card,
                        parent,
                        false
                    )
                )
            TYPE_FEED_DEV ->
                FeedDevTitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_dev_layout,
                        parent,
                        false
                    )
                )
            TYPE_FEED_BOTTOM_PADDING -> {
                FeedPaddingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_padding,
                        parent,
                        false
                    )
                )
            }
            TYPE_FEED_NETWORK_ERROR ->
                FeedNetworkErrorTitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_network_error,
                        parent,
                        false
                    )
                )
            TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_TITLE ->
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
                        R.layout.item_main_feed_title,
                        parent,
                        false
                    )
                )
            TYPE_FEED_LOAD_MORE ->
                FeedLoadMoreViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_normal_loading,
                        parent,
                        false
                    )
                )
            TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS ->
                FeedTodayPlanBtnTipsHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_today_plans_remind_layout,
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
            TYPE_FEED_QUICK_BTNS ->
                FeedQuickBtnsViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_quick_btns,
                        parent,
                        false
                    )
                )
            TYPE_FEED_TODAY_PLANS_ENCOURAGE -> {
                FeedTodayPlanEncourageViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_today_plans_encourage,
                        parent,
                        false
                    )
                )
            }
            TYPE_FEED_NO_CONTENT -> {
                FeedNoContentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_no_more_content,
                        parent,
                        false
                    )
                )
            }
            else ->
                NewStyleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_main_feed_aritcle_normal,
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedPunchResp -> TYPE_FEED_PUNCH
            is FeedTodayPlanResp -> TYPE_FEED_TODAY_PLAN
            is FeedQuickBtnsResp -> TYPE_FEED_QUICK_BTNS
            is FeedDateResp -> TYPE_FEED_DATE
            is FeedQuickEditNewPlanResp -> TYPE_FEED_EDIT_NEW_PLAN
            is FeedNetworkErrorTitleResp -> TYPE_FEED_NETWORK_ERROR
            is FeedTimeLineFeedTitleResp -> TYPE_FEED_TIME_LINE_FEED_TITLE
            is FeedTimeLineFeedTodayPlansResp -> TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN
            is FeedTimeLineFeedTodayPlansTitleResp -> TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_TITLE
            is FeedTimeLineFeedTodayPlansTipsTitleResp -> TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS
            is FeedDevTitleResp -> TYPE_FEED_DEV
            is FeedQuestionFeedResp -> TYPE_FEED_QUESTION_CARD
            is FeedArticleFeedResp -> TYPE_FEED_NORMAL_CARD
            is FeedEncourageTipsResp -> TYPE_FEED_TODAY_PLANS_ENCOURAGE
            is FeedLoadMoreResp -> TYPE_FEED_LOAD_MORE
            is FeedNoContentResp -> TYPE_FEED_NO_CONTENT
            is FeedBottomPaddingResp -> TYPE_FEED_BOTTOM_PADDING
            else -> TYPE_FEED_NORMAL_CARD
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
        } else if (payloads.contains(PAYLOAD_TODAY_PLANS_STATUS)) {
            if (holder is FeedTimeLineFeedTodayPlansViewHolder) {
                holder.payloadBind(getItem(position) as FeedTimeLineFeedTodayPlansResp)
            }
        } else if (payloads.contains(PAYLOAD_TODAY_PLANS_EDIT)) {
            if (holder is FeedTimeLineFeedTodayPlansViewHolder) {
                holder.payloadBindContent(getItem(position) as FeedTimeLineFeedTodayPlansResp)
            }
        } else if (payloads.contains(PAYLOAD_TIME_UPDATE)) {
            if (holder is FeedDateViewHolder) {
                holder.payload()
            }
        } else if (payloads.contains(PAYLOAD_HAS_END_LINE)) {
            if (holder is FeedTimeLineFeedTodayPlansViewHolder) {
                holder.payloadEndLine(getItem(position).hideEndLine)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeedPunchViewHolder -> holder.bind((getItem(position) as FeedPunchResp))
            is NormalViewHolder -> holder.bind((getItem(position) as FeedArticleFeedResp))
            is FeedDateViewHolder -> holder.bind()
            is FeedLoadMoreViewHolder -> holder.bind()
            is NewStyleViewHolder -> {
                (getItem(position) as? FeedArticleFeedResp)?.let {
                    holder.bind(it)
                }
            }
            is FeedQuestionViewHolder -> {
                (getItem(position) as? FeedQuestionFeedResp)?.let {
                    holder.bind(it)
                }
            }
            is FeedTimeLineFeedTodayPlansViewHolder -> holder.bind((getItem(position) as FeedTimeLineFeedTodayPlansResp))
            is FeedTodayPlanBtnTipsHolder -> holder.bind((getItem(position) as FeedTimeLineFeedTodayPlansTipsTitleResp))
        }
    }

    inner class FeedPunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(params: FeedPunchResp) {
            listener?.let { listener ->
                itemView.setDebounceOnClickListener {
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

    inner class FeedTodayPlanBtnTipsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            listener?.let { listener ->
                itemView.findViewById<RoundCornerConstraintLayout>(R.id.layout_remind)
                    .setDebounceOnClickListener {
                        listener.onClick(TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS, null)
                    }
                itemView.findViewById<TextView>(R.id.btn_no_suc_plan).setDebounceOnClickListener {
                    listener.onClick(TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_APPLY_OLD, null)
                }
            }
            itemView.findViewById<TimeLineView>(R.id.time_line).hideBottomLine()
        }

        fun bind(bean: FeedTimeLineFeedTodayPlansTipsTitleResp) {
            itemView.findViewById<TextView>(R.id.btn_no_suc_plan).visibility =
                if (bean.showApply) View.VISIBLE else View.GONE
        }
    }

    inner class FeedDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay = itemView.findViewById<TextView>(R.id.tv_day)
        private val tvMonth = itemView.findViewById<TextView>(R.id.tv_month)
        private val tvWeek = itemView.findViewById<TextView>(R.id.tv_week)
        private val btnLogin = itemView.findViewById<TextView>(R.id.btn_login)
        private val btnSetting = itemView.findViewById<AppCompatImageView>(R.id.btn_settings)

        fun bind() {
            btnSetting.setOnClickListener {
                normalListener?.onClickSetting()
            }
            btnLogin.setOnClickListener {
                listener?.onClick(CLICK_MAIN_FEED_LOGIN, null)
            }
            AppConfig.getUserInfo()?.let {
                btnLogin.text = it.username
            }
            val time = System.currentTimeMillis()
            tvMonth.text = "${TimeUtils.getMonth(time)}月"
            tvDay.text = "${TimeUtils.getDay(time)}日"
            tvWeek.text = TimeUtils.getDayOfWeek(time)
        }

        fun payload() {
            val time = System.currentTimeMillis()
            tvMonth.text = "${TimeUtils.getMonth(time)}月"
            tvDay.text = "${TimeUtils.getDay(time)}日"
            tvWeek.text = TimeUtils.getDayOfWeek(time)
        }
    }

    inner class FeedTimeLineFeedTodayPlansViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.text)
        private val lineTimeSchedule = itemView.findViewById<View>(R.id.line_time_schedule)
        private val tvCreateTime = itemView.findViewById<TextView>(R.id.tv_create_time)
        private val tvTimeSchedule = itemView.findViewById<TextView>(R.id.tv_time_schedule)
        private val timeLine = itemView.findViewById<TimeLineView>(R.id.time_line)
        private val tvModule = itemView.findViewById<TextView>(R.id.tv_module)
        private val cb = itemView.findViewById<AppCompatCheckBox>(R.id.cb_today_plan)

        fun bind(resp: FeedTimeLineFeedTodayPlansResp) {
            reset()
            title.text = resp.params.beanSingle.content
            tvModule.text = resp.params.beanSingle.moduleName
            lineTimeSchedule.visibility = View.VISIBLE
            tvTimeSchedule.visibility = View.VISIBLE
            tvTimeSchedule.text = resp.params.timeSchedule?.let {
                "预期在：${TimeUtils.getDateChinese(it.first)} - ${TimeUtils.getHourM(it.second)} 内完成"
            } ?: "创建时间：${TimeUtils.getDateChinese(resp.date)}"

            bindSelect(resp.params.statusSingle == SinglePlanStatus.SELECT)
            cb.isChecked = resp.params.statusSingle == SinglePlanStatus.SELECT
            cb.setOnCheckedChangeListener { _, isChecked ->
                checkListener?.onCheck(resp, isChecked)
            }
            payloadEndLine(resp.hideEndLine)
        }

        fun payloadEndLine(goneEndLie: Boolean) {
            if (goneEndLie) {
                timeLine.hideBottomLine()
            } else {
                timeLine.showBottomLine()
            }
        }

        fun payloadBindContent(resp: FeedTimeLineFeedTodayPlansResp) {
            title.text = resp.params.beanSingle.content
            resp.params.timeSchedule?.let {
                tvTimeSchedule.visibility = View.VISIBLE
                lineTimeSchedule.visibility = View.VISIBLE
                tvTimeSchedule.text =
                    "${TimeUtils.getDateChinese(it.first)} - ${TimeUtils.getHourM(it.second)}"
            } ?: also {
                tvTimeSchedule.visibility = View.GONE
                lineTimeSchedule.visibility = View.GONE
            }
            bindSelect(resp.params.statusSingle == SinglePlanStatus.SELECT)
        }

        fun payloadBind(resp: FeedTimeLineFeedTodayPlansResp) {
            bindSelect(resp.params.statusSingle == SinglePlanStatus.SELECT)
        }

        private fun reset() {
            cb.setOnCheckedChangeListener(null)
        }

        private fun bindSelect(select: Boolean) {
            if (select) {
                timeLine.useImageView(true)
                title.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
                title.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
                tvModule.setBackgroundResource(R.drawable.bg_round_grey_500)
                tvModule.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
                tvModule.setBackgroundResource(R.drawable.bg_round_grey_500)
            } else {
                timeLine.useImageView(false)
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

    inner class FeedPaddingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    inner class FeedLoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            normalListener?.loadedMore()
        }
    }

    inner class FeedTimeLineFeedTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class FeedDevTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<LinearLayout>(R.id.btn_hide).setOnClickListener {
                clickListener?.onClick(TYPE_FEED_DEV)
            }
        }
    }

    inner class FeedNetworkErrorTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TimeLineView>(R.id.time_line).hideBottomLine()
        }
    }

    inner class FeedNoContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TextView>(R.id.btn_go_ugc).apply {
                setOnClickListener {
                    normalListener?.onClickGoUGC()
                }
                paint.flags = Paint.UNDERLINE_TEXT_FLAG
                paint.isAntiAlias = true
            }
        }
    }

    inner class FeedQuickBtnsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            listener?.let { listener ->
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_plan)
                    .setDebounceOnClickListener {
                        listener.onClick(CLICK_QUICK_BTN_PLAN, null)
                    }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_ugc)
                    .setDebounceOnClickListener {
                        listener.onClick(CLICK_QUICK_BTN_UGC, null)
                    }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_me)
                    .setDebounceOnClickListener {
                        listener.onClick(CLICK_QUICK_BTN_ME, null)
                    }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_history)
                    .setDebounceOnClickListener {
                        listener.onClick(CLICK_QUICK_BTN_HISTORY_PLANS, null)
                    }
                itemView.findViewById<ConstraintLayout>(R.id.layout_btn_time_schedule)
                    .setDebounceOnClickListener {
                        listener.onClick(CLICK_QUICK_BTN_TIME_SCHEDULE, null)
                    }
            }
            itemView.findViewById<LinearLayout>(R.id.btn_hide).setOnClickListener {
                clickListener?.onClick(TYPE_FEED_QUICK_BTNS)
            }
        }
    }

    inner class FeedTodayPlanEncourageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class FeedQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAnswer = itemView.findViewById<TextView>(R.id.tv_answer)
        private val flex = itemView.findViewById<FlexboxLayout>(R.id.flex)

        fun bind(question: FeedQuestionFeedResp) {
            itemView.findViewById<TextView>(R.id.tv_title).text = question.question.title
            itemView.findViewById<TextView>(R.id.tv_name).text = TimeUtils.getDateChinese(question.question.makeTime)
            itemView.findViewById<TextView>(R.id.tv_content).text = question.question.content
            itemView.findViewById<TextView>(R.id.btn_quick).setDebounceOnClickListener {
                questionListener?.onClick(TYPE_FEED_QUESTION_CARD_CLICK_QUICK_SEND, question)
            }
            itemView.setDebounceOnClickListener {
                questionListener?.onClick(TYPE_FEED_QUESTION_CARD_CLICK_QUICK_SEND, question)
            }

            question.answer.let {
                val username = it.userInfo?.username ?: "匿名用户"
                if (it.listStyle == QUESTION_QUICK_SEND_LIST_STYLE) {
                    GsonProvider.getDefaultGson().fromJson(it.content, TodayPlansEntity::class.java)
                        .let {
                            val str = "$username：我的计划，参考一下？"
                            UIUtils.setTextViewSpanColor(
                                tvAnswer, str, 0, username.length, ContextCompat.getColor(
                                    itemView.context,
                                    R.color.amber_600
                                )
                            )
                            flex.removeAllViews()
                            it.resp.params.flatMap {
                                it.beanSingles
                            }.forEach {
                                flex.addView(
                                    TextView(flex.context).apply {
                                        layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        text = it.beanSingle.content ?: "!!!"
                                    })
                            }
                        }
                } else {
                    val str = "$username：${it.content ?: ""}"
                    UIUtils.setTextViewSpanColor(
                        tvAnswer, str, 0, username.length, ContextCompat.getColor(
                            itemView.context,
                            R.color.amber_600
                        )
                    )
                }
            }
        }
    }

    inner class FeedEditNewPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val et = itemView.findViewById<EditText>(R.id.et_new_plan)
        private val btnSend = itemView.findViewById<AppCompatImageView>(R.id.btn_send_new_plan)

        init {
            et.clearFocus()
            normalListener?.let { listener ->
                btnSend.setDebounceOnClickListener {
                    val str = et.text.toString()
                    if (str.isNotBlank()) {
                        listener.onSendNewPlanClick(str)
                        et.setText("")
                    } else {
                        ToastUtil.toast("空计划，不大好吧~")
                    }
                }
            }
        }
    }

    inner class NewStyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeLine = itemView.findViewById<TimeLineView>(R.id.time_line)
        fun bind(data: FeedArticleFeedResp) {
            reset()
            listener?.let { listener ->
                itemView.setDebounceOnClickListener {
                    listener.onClick(TYPE_TOP_ONE, data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.makeTime?.let {
                it
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

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FeedArticleFeedResp) {
            listener?.let { listener ->
                itemView.setDebounceOnClickListener {
                    listener.onClick(TYPE_FEED_NORMAL_CARD, data)
                }
            }
            itemView.findViewById<TextView>(R.id.tv_title).text = data.article.title
            itemView.findViewById<TextView>(R.id.tv_name).text = data.article.makeTime
            itemView.findViewById<TextView>(R.id.tv_content).text = data.article.content
        }
    }
}

interface OnNormalFeedListener {
    fun onSendNewPlanClick(content: String)
    fun loadedMore()
    fun onClickGoUGC()
    fun onClickSetting()
}

interface OnMainFeedClickListener {
    fun onClick(type: Int, respFeed: FeedArticleFeedResp?)
}

interface OnMainFeedQuickClickListener {
    fun onClick(type: Int, questionResp: FeedQuestionFeedResp)
}

interface OnMainFeedTodayPlanCheckListener {
    fun onCheck(resp: FeedTimeLineFeedTodayPlansResp, isCheck: Boolean)
}

interface OnMainFeedHideClickListener {
    fun onClick(type: Int)
}