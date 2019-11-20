package com.mdove.civilservantcommunity.plan.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.model.HistoryPlansBaseBean
import com.mdove.civilservantcommunity.plan.model.HistoryPlansEmptyTipsBean
import com.mdove.civilservantcommunity.plan.model.HistoryPlansSinglePlanBean
import com.mdove.civilservantcommunity.plan.model.SinglePlanStatus
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.view.timeline.TimeLineView

/**
 * Created by MDove on 2019-11-14.
 */
class HistoryPlansAdapter : ListAdapter<HistoryPlansBaseBean, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<HistoryPlansBaseBean>() {
    override fun areItemsTheSame(
        oldItem: HistoryPlansBaseBean,
        newItem: HistoryPlansBaseBean
    ): Boolean {
        return if ((oldItem as? HistoryPlansSinglePlanBean)?.statusSingle == (newItem as? HistoryPlansSinglePlanBean)?.statusSingle) {
            true
        } else (oldItem is HistoryPlansEmptyTipsBean) && (newItem is HistoryPlansEmptyTipsBean)
    }

    override fun areContentsTheSame(
        oldItem: HistoryPlansBaseBean,
        newItem: HistoryPlansBaseBean
    ): Boolean {
        return if ((oldItem as? HistoryPlansSinglePlanBean)?.beanSingle?.content == (newItem as? HistoryPlansSinglePlanBean)?.beanSingle?.content) {
            true
        } else (oldItem is HistoryPlansEmptyTipsBean) && (newItem is HistoryPlansEmptyTipsBean)
    }

    override fun getChangePayload(
        oldItem: HistoryPlansBaseBean,
        newItem: HistoryPlansBaseBean
    ): Any? {
        return if ((oldItem as? HistoryPlansSinglePlanBean)?.beanSingle?.content == (newItem as? HistoryPlansSinglePlanBean)?.beanSingle?.content) {
            PAYLOAD_STATUS
        } else null
    }

}) {
    companion object {
        val PAYLOAD_STATUS = Any()

        const val TYPE_SINGLE_PLAN = 1
        const val TYPE_EMPTY_TIPS = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoryPlansSinglePlanBean -> {
                TYPE_SINGLE_PLAN
            }
            is HistoryPlansEmptyTipsBean -> {
                TYPE_EMPTY_TIPS
            }
            else -> {
                TYPE_SINGLE_PLAN
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY_TIPS -> {
                EmptyTipsViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_history_plan_empty_tips,
                        parent,
                        false
                    )
                )
            }
            else -> {
                SingleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_history_plan_single,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? SingleViewHolder)?.bind(getItem(position) as HistoryPlansSinglePlanBean)
    }

    inner class SingleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.text)
        private val lineTimeSchedule = itemView.findViewById<View>(R.id.line_time_schedule)
        private val tvTimeSchedule = itemView.findViewById<TextView>(R.id.tv_time_schedule)
        private val tvModule = itemView.findViewById<TextView>(R.id.tv_module)
        private val layoutComplete = itemView.findViewById<ConstraintLayout>(R.id.layout_complete)

        fun bind(planBean: HistoryPlansSinglePlanBean) {
            title.text = planBean.beanSingle.content
            tvModule.text = planBean.beanSingle.moduleName
            planBean.timeSchedule?.let {
                lineTimeSchedule.visibility = View.VISIBLE
                tvTimeSchedule.visibility = View.VISIBLE
                tvTimeSchedule.text =
                    "预期${TimeUtils.getDateChinese(it.first)} - ${TimeUtils.getHourM(it.second)}之内完成"
            } ?: also {
                tvTimeSchedule.visibility = View.GONE
                lineTimeSchedule.visibility = View.GONE
            }
            if (planBean.statusSingle == SinglePlanStatus.SELECT) {
                layoutComplete.visibility = View.VISIBLE
                title.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                tvTimeSchedule.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                title.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
                lineTimeSchedule.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
                tvTimeSchedule.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
                tvModule.setBackgroundResource(R.drawable.bg_round_grey_500)
                tvModule.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                tvModule.setBackgroundResource(R.drawable.bg_round_grey_500)
            } else {
                layoutComplete.visibility = View.GONE
                title.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                title.paint.flags = 0
                title.paint.flags = Paint.ANTI_ALIAS_FLAG
                lineTimeSchedule.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black))
                tvTimeSchedule.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                tvTimeSchedule.paint.flags = 0
                tvTimeSchedule.paint.flags = Paint.ANTI_ALIAS_FLAG
                tvModule.setBackgroundResource(R.drawable.bg_round_blue)
                tvModule.paint.flags = 0
                tvModule.paint.flags = Paint.ANTI_ALIAS_FLAG
            }
        }
    }

    inner class EmptyTipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}