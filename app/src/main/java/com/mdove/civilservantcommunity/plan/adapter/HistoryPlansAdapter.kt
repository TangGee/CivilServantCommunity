package com.mdove.civilservantcommunity.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.model.HistoryPlansBaseBean
import com.mdove.civilservantcommunity.plan.model.HistoryPlansEmptyTipsBean
import com.mdove.civilservantcommunity.plan.model.HistoryPlansSinglePlanBean
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
        private val timeLine = itemView.findViewById<TimeLineView>(R.id.time_line)
        private val tvModule = itemView.findViewById<TextView>(R.id.tv_module)

        fun bind(planBean: HistoryPlansSinglePlanBean) {
            title.text = planBean.beanSingle.content
            tvModule.text = planBean.beanSingle.moduleName
            planBean.timeSchedule?.let {
                lineTimeSchedule.visibility = View.VISIBLE
                tvTimeSchedule.visibility = View.VISIBLE
                tvTimeSchedule.text =
                    "${TimeUtils.getDateChinese(it.first)} - ${TimeUtils.getHourM(it.second)}"
            } ?: also {
                tvTimeSchedule.visibility = View.GONE
                lineTimeSchedule.visibility = View.GONE
            }
        }
    }

    inner class EmptyTipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}