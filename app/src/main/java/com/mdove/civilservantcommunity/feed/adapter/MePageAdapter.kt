package com.mdove.civilservantcommunity.feed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.bean.BaseMePageDetailInfo
import com.mdove.civilservantcommunity.account.bean.MePageAnswerInfoMePage
import com.mdove.civilservantcommunity.account.bean.MePageArticleInfoMePage
import com.mdove.civilservantcommunity.account.bean.MePageQuestionInfoMePage
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener

/**
 * Created by MDove on 2019-09-11.
 */
class MePageAdapter(val listener: OnMePageClickListener? = null) :
    ListAdapter<BaseMePageDetailInfo, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<BaseMePageDetailInfo>() {
        override fun areItemsTheSame(
            oldItem: BaseMePageDetailInfo,
            newItem: BaseMePageDetailInfo
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: BaseMePageDetailInfo,
            newItem: BaseMePageDetailInfo
        ): Boolean {
            return true
        }
    }) {
    private val TYPE_NORMAL = 0
    private val TYPE_QUESTION = 1
    private val TYPE_QUESTION_ANSWER = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_QUESTION -> QuestionViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_me_page_question,
                    parent,
                    false
                )
            )
            TYPE_QUESTION_ANSWER -> QuestionAnswerViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_me_page_question_answer,
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
        return when (getItem(position)) {
            is MePageQuestionInfoMePage -> TYPE_QUESTION
            is MePageAnswerInfoMePage -> TYPE_QUESTION_ANSWER
            is MePageArticleInfoMePage -> TYPE_NORMAL
            else -> TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is QuestionViewHolder -> holder.bind(getItem(position) as MePageQuestionInfoMePage)
            is QuestionAnswerViewHolder -> holder.bind(getItem(position) as MePageAnswerInfoMePage)
            is FeedMeViewHolder -> holder.bind(getItem(position) as MePageArticleInfoMePage)
        }
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)

        fun bind(data: MePageQuestionInfoMePage) {
            itemView.setDebounceOnClickListener {
                listener?.onClickQuestion(data)
            }
            data.question?.let {
                tvTime.visibility = View.VISIBLE
                tvContent.text = it.content ?: itemView.context.getString(R.string.string_no_content)
                tvTitle.text = it.title ?: itemView.context.getString(R.string.string_no_content)
            } ?: also {
                tvTime.visibility = View.GONE
                tvContent.text = itemView.context.getString(R.string.string_no_content)
                tvTitle.text = itemView.context.getString(R.string.string_no_content)
            }
        }
    }

    inner class QuestionAnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvAnswer = itemView.findViewById<TextView>(R.id.tv_answer)
        private val tvQuestionUser = itemView.findViewById<TextView>(R.id.tv_question_user)

        fun bind(data: MePageAnswerInfoMePage) {
            itemView.setDebounceOnClickListener {
                listener?.onClickAnswer(data)
            }
            data.question?.let {
                val userName = it.userInfo?.username ?: "匿名用户"
                UIUtils.setTextViewSpanColor(
                    tvQuestionUser,
                    userName.plus("求攻略"),
                    0,
                    userName.length,
                    ContextCompat.getColor(itemView.context, R.color.amber_600)
                )
            }
            data.answer?.let {
                tvTime.text = TimeUtils.getDateChinese(it.makeTime)
                UIUtils.setTextViewSpanColor(
                    tvAnswer,
                    "我的回答：${it.content}",
                    0,
                    4,
                    ContextCompat.getColor(itemView.context, R.color.amber_600)
                )
                tvContent.text = data.question?.content ?: itemView.context.getString(R.string.string_no_content)
                tvTitle.text = data.question?.title ?: itemView.context.getString(R.string.string_no_content)
            }
        }
    }

    inner class FeedMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)

        fun bind(data: MePageArticleInfoMePage) {
            itemView.setDebounceOnClickListener {
                listener?.onClickArticle(data)
            }
            tvTitle.text = data.title
            tvTime.text = data.maketime
            tvContent.text = data.content
        }
    }
}

interface OnMePageClickListener {
    fun onClickArticle(article: MePageArticleInfoMePage)
    fun onClickQuestion(article: MePageQuestionInfoMePage)
    fun onClickAnswer(article: MePageAnswerInfoMePage)
}