package com.mdove.civilservantcommunity.question.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.question.bean.AnswerDetailBean
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener

/**
 * Created by MDove on 2019-11-24.
 */
class QuestionAdapter(val listener: OnClickQuestionListener? = null) :
    ListAdapter<AnswerDetailBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<AnswerDetailBean>() {
        override fun areItemsTheSame(
            oldItem: AnswerDetailBean,
            newItem: AnswerDetailBean
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: AnswerDetailBean,
            newItem: AnswerDetailBean
        ): Boolean {
            return true
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnswerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_question_answer_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AnswerViewHolder)?.bind(getItem(position))
    }

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUser = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvAnswer = itemView.findViewById<TextView>(R.id.tv_answer)
        private val btnGo = itemView.findViewById<TextView>(R.id.btn_go)
        private val btnReply = itemView.findViewById<TextView>(R.id.btn_reply)
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)

        fun bind(bean: AnswerDetailBean) {
            tvUser.text = bean.an?.userInfo?.username ?: itemView.context.getText(R.string.string_no_name)
            tvContent.text = bean.an?.content ?: "无效发文，暂时隐藏..."
            tvTime.text = TimeUtils.getDateChinese(bean.an?.makeTime)
            bean.playCommentOnelist?.firstOrNull()?.let {
                btnReply.visibility = View.GONE
                val userName = "来自${it.info?.userName ?: itemView.context.getText(R.string.string_no_name)}"
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
}