package com.mdove.civilservantcommunity.question.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
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
import com.mdove.civilservantcommunity.question.bean.BaseQuestionCommentBean
import com.mdove.civilservantcommunity.question.bean.QuestionCommentBean
import com.mdove.civilservantcommunity.question.bean.QuestionCommentPairBean
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener
import com.mdove.dependent.common.view.span.RadiusBackgroundSpan

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
                false
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
        return when (getItem(position)) {
            is QuestionCommentBean -> 1
            is QuestionCommentPairBean -> 2
            else -> 1
        }
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
            val fromName = data.child.commentInfo?.userName
                ?: itemView.context.getString(R.string.string_no_name)
            val toName = data.child.toInfo?.toUserName
                ?: itemView.context.getString(R.string.string_no_name)
            val name = "$fromName 对 $toName"
            tvName.text = name
            tvContent.text =
                data.child.content ?: itemView.context.getString(R.string.string_no_content)
            tvTime.text = TimeUtils.getDateChinese(data.child.makeTime)

            val spannableString = SpannableString(name)
            val span1 = ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.black))
            spannableString.setSpan(
                span1,
                0,
                fromName.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            val bg1 = RadiusBackgroundSpan(
                ContextCompat.getColor(itemView.context, R.color.grey_200),
                UIUtils.dip2Px(itemView.context, 2).toInt()
            )
            spannableString.setSpan(
                bg1,
                0,
                fromName.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            val bg2 = RadiusBackgroundSpan(
                ContextCompat.getColor(itemView.context, R.color.grey_200),
                UIUtils.dip2Px(itemView.context, 2).toInt()
            )
            spannableString.setSpan(
                bg2,
                fromName.length + 3,
                fromName.length + 3 + toName.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            tvName.text = spannableString

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