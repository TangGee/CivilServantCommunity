package com.mdove.civilservantcommunity.question.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.adapter.TYPE_NORMAL_ERROR_ICON
import com.mdove.civilservantcommunity.base.adapter.TYPE_NORMAL_ERROR_TITLE
import com.mdove.civilservantcommunity.base.adapter.createNormalErrorIconViewHolder
import com.mdove.civilservantcommunity.base.adapter.createNormalErrorTitleViewHolder
import com.mdove.civilservantcommunity.question.bean.*
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener

/**
 * Created by MDove on 2019-11-24.
 */
class DetailQuestionAdapter(val listener: OnClickQuestionListener? = null) :
    ListAdapter<BaseDetailQuestionBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<BaseDetailQuestionBean>() {
        override fun areItemsTheSame(
            oldItem: BaseDetailQuestionBean,
            newItem: BaseDetailQuestionBean
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: BaseDetailQuestionBean,
            newItem: BaseDetailQuestionBean
        ): Boolean {
            return true
        }
    }) {

    companion object {
        const val TYPE_ANSWER = 1
        const val TYPE_SEND = 3
        const val TYPE_HEAD_QUESTION = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AnswerDetailBean -> {
                TYPE_ANSWER
            }
            is DetailQuestionSendBean -> {
                TYPE_SEND
            }
            is QuestionDetailErrorBean -> {
                TYPE_NORMAL_ERROR_TITLE
            }
            is DetailQuestionErrorIconBean -> {
                TYPE_NORMAL_ERROR_ICON
            }
            else -> {
                TYPE_HEAD_QUESTION
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ANSWER -> {
                AnswerViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_detail_question_answer,
                        parent,
                        false
                    )
                )
            }
            TYPE_NORMAL_ERROR_TITLE -> {
                parent.createNormalErrorTitleViewHolder(parent.context.getString(R.string.req_detail_question_error))
            }
            TYPE_SEND -> {
                DetailQuestionSendViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_detail_question_send_answer, parent,
                        false
                    )
                )
            }
            TYPE_NORMAL_ERROR_ICON -> {
                parent.createNormalErrorIconViewHolder()
            }
            TYPE_HEAD_QUESTION -> {
                DetailQuestionViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_detail_question_head, parent,
                        false
                    )
                )
            }
            else -> {
                DetailQuestionViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_detail_question_head, parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AnswerViewHolder)?.bind(getItem(position) as AnswerDetailBean)
        (holder as? DetailQuestionViewHolder)?.bind(getItem(position) as QuestionDetailBean)
    }

    inner class DetailQuestionSendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setDebounceOnClickListener {
                listener?.onClickSendAnswer()
            }
        }
    }

    inner class DetailQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuestionUser = itemView.findViewById<TextView>(R.id.tv_question_user)
        private val tvQuestionTitle = itemView.findViewById<TextView>(R.id.tv_question_title)
        private val tvQuestionContent = itemView.findViewById<TextView>(R.id.tv_question_content)
        private val tvQuestionTime = itemView.findViewById<TextView>(R.id.tv_question_time)

        fun bind(bean: QuestionDetailBean) {
            val userName = "来自：${bean.userInfo?.username ?: "匿名用户"}"
            UIUtils.setTextViewSpanColor(
                tvQuestionUser,
                userName,
                3,
                userName.length,
                ContextCompat.getColor(
                    itemView.context,
                    R.color.blue_500
                )
            )
            tvQuestionTitle.text = bean.title ?: "无标题"
            tvQuestionContent.text = bean.content ?: "无内容"
            tvQuestionTime.text = TimeUtils.getDateChinese(bean.makeTime)
        }
    }

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUser = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvAnswer = itemView.findViewById<TextView>(R.id.tv_answer)
        private val btnGo = itemView.findViewById<TextView>(R.id.btn_go)
        private val btnReply = itemView.findViewById<TextView>(R.id.btn_reply)
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)

        fun bind(bean: AnswerDetailBean) {
            tvUser.text =
                bean.an?.userInfo?.username ?: itemView.context.getText(R.string.string_no_name)
            tvContent.text = bean.an?.content ?: "无效发文，暂时隐藏..."
            tvTime.text = TimeUtils.getDateChinese(bean.an?.makeTime)
            bean.playCommentOnelist?.firstOrNull()?.let {
                btnReply.visibility = View.GONE
                val userName =
                    "来自${it.info?.userName ?: itemView.context.getText(R.string.string_no_name)}"
                val realContent = userName.plus("的评论：").plus(it.content ?: "无效发文，暂时隐藏...")
                tvAnswer.visibility = View.VISIBLE
                btnGo.visibility = View.VISIBLE
                UIUtils.setTextViewSpanColor(
                    tvAnswer, realContent, 2, userName.length, ContextCompat.getColor(
                        itemView.context,
                        R.color.blue_500
                    )
                )
            } ?: also {
                btnReply.visibility = View.VISIBLE
                tvAnswer.visibility = View.GONE
                btnGo.visibility = View.INVISIBLE
            }
            btnReply.setDebounceOnClickListener {
                listener?.onClickReply(bean)
            }
            itemView.setDebounceOnClickListener {
                listener?.onClickMore(bean)
            }
        }
    }
}

interface OnClickQuestionListener {
    fun onClickMore(bean: AnswerDetailBean)
    fun onClickReply(bean: AnswerDetailBean)
    fun onClickSendAnswer()
}