package com.mdove.civilservantcommunity.question.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.question.bean.BaseQuestionCommentBean
import com.mdove.civilservantcommunity.question.bean.QuestionCommentBean
import com.mdove.civilservantcommunity.question.bean.QuestionCommentPairBean
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener

/**
 * Created by MDove on 2019-11-24.
 */
class QuestionCommentAdapter(val listener: OnQuestionCommentListener? = null) :
    ListAdapter<BaseQuestionCommentBean, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<BaseQuestionCommentBean>() {
        override fun areItemsTheSame(
            oldItem: BaseQuestionCommentBean,
            newItem: BaseQuestionCommentBean
        ): Boolean {
            return if (oldItem is QuestionCommentBean && newItem is QuestionCommentBean) {
                oldItem.content == newItem.content && oldItem.info?.uid == newItem.info?.uid
            } else if (oldItem is QuestionCommentPairBean && newItem is QuestionCommentPairBean) {
                oldItem.child.content == newItem.child.content
            } else {
                true
            }
        }

        override fun areContentsTheSame(
            oldItem: BaseQuestionCommentBean,
            newItem: BaseQuestionCommentBean
        ): Boolean {
            return true
        }
    }) {

    override fun getItemViewType(position: Int): Int {
        when (getItem(position)) {
            is QuestionCommentBean -> 1
            is QuestionCommentPairBean -> 2
            else -> 1
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                CommentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_question_comment,
                        parent,
                        false
                    )
                )
            }
            2 -> CommentChildViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_question_comment_child,
                    parent,
                    false
                )
            )
            else -> {
                CommentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_question_comment,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CommentViewHolder)?.bind(getItem(position) as QuestionCommentBean)
        (holder as? CommentChildViewHolder)?.bind(getItem(position) as QuestionCommentPairBean)
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        private val btnSend = itemView.findViewById<AppCompatImageView>(R.id.btn_send_comment)

        fun bind(data: QuestionCommentBean) {
            tvContent.text = data.content ?: itemView.context.getString(R.string.string_no_content)
            tvName.text = data.info?.userName ?: itemView.context.getString(R.string.string_no_name)
            tvTime.text = TimeUtils.getDateChinese(data.makeTime)
            btnSend.setDebounceOnClickListener {
                listener?.onClickSendComment(data)
            }
        }
    }

    inner class CommentChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        private val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        private val btnSend = itemView.findViewById<AppCompatImageView>(R.id.btn_send_comment)

        fun bind(data: QuestionCommentPairBean) {
            val name = "${data.child.commentInfo?.userName
                ?: itemView.context.getString(R.string.string_no_name)} 对 ${data.father.info?.userName
                ?: itemView.context.getString(R.string.string_no_name)}"
            tvName.text = name
            tvContent.text =
                data.child.content ?: itemView.context.getString(R.string.string_no_content)
            tvTime.text = TimeUtils.getDateChinese(data.child.makeTime)
            btnSend.setDebounceOnClickListener {
                listener?.onClickSendCommentChild(data)
            }
        }
    }
}

interface OnQuestionCommentListener {
    fun onClickSendComment(data: QuestionCommentBean)
    fun onClickSendCommentChild(data: QuestionCommentPairBean)
}