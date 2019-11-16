package com.mdove.civilservantcommunity.plan.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.model.SinglePlanBean
import com.mdove.civilservantcommunity.plan.model.SinglePlanBeanWrapper
import com.mdove.civilservantcommunity.plan.model.SinglePlanStatus
import com.mdove.civilservantcommunity.plan.model.SinglePlanType
import com.mdove.dependent.common.toast.ToastUtil

class EditSinglePlanAdapter(private val listener: OnSinglePlanClickListener? = null) :
    ListAdapter<SinglePlanBeanWrapper, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<SinglePlanBeanWrapper>() {
        override fun areContentsTheSame(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Boolean {
            return oldItem.statusSingle == newItem.statusSingle
                    && oldItem.beanSingle.content == newItem.beanSingle.content ||
                    oldItem.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
                    && newItem.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
        }

        override fun areItemsTheSame(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Boolean {
            return oldItem.statusSingle == newItem.statusSingle
                    && oldItem.beanSingle.content == newItem.beanSingle.content ||
                    oldItem.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
                    && newItem.typeSingle == SinglePlanType.CUSTOM_PLAN_BTN
        }

        override fun getChangePayload(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Any? {
            return if (oldItem.statusSingle != newItem.statusSingle
                && oldItem.typeSingle == SinglePlanType.SYS_PLAN
                && newItem.typeSingle == SinglePlanType.SYS_PLAN
            ) {
                PAYLOAD_SINGLE_PLAN_DELETE
            } else if (oldItem.beanSingle.content != newItem.beanSingle.content) {
                PAYLOAD_SINGLE_PLAN_EDIT
            } else if (oldItem.statusSingle != newItem.statusSingle
                && oldItem.typeSingle == SinglePlanType.CUSTOM_PLAN
                && newItem.typeSingle == SinglePlanType.CUSTOM_PLAN
            ) {
                PAYLOAD_CUSTOM_PLAN
            } else {
                null
            }
        }
    }) {

    companion object {
        val PAYLOAD_SINGLE_PLAN_DELETE = Any()
        val PAYLOAD_SINGLE_PLAN_EDIT = Any()
        val PAYLOAD_CUSTOM_PLAN = Any()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position).typeSingle == SinglePlanType.CUSTOM_PLAN_BTN -> 1
            getItem(position).typeSingle == SinglePlanType.CUSTOM_PLAN -> 2
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> return SinglePlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_plan_single_plan,
                    parent,
                    false
                )
            )
            2 -> return CustomSinglePlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_plan_custom_single_plan,
                    parent,
                    false
                )
            )
            else -> return SinglePlanCustomBtnViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_plan_custom_single_et_plan,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SinglePlanViewHolder -> {
                holder.bind(wrapper = getItem(position))
            }
            is CustomSinglePlanViewHolder -> {
                holder.bind(wrapper = getItem(position))
            }
            is SinglePlanCustomBtnViewHolder -> {
                holder.bind(data = getItem(position).beanSingle)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when {
            payloads.contains(PAYLOAD_SINGLE_PLAN_DELETE) -> (holder as? SinglePlanViewHolder)?.payload(
                getItem(position)
            )
            payloads.contains(PAYLOAD_SINGLE_PLAN_EDIT) -> (holder as? SinglePlanViewHolder)?.payloadContent(
                getItem(position)
            )
            payloads.contains(PAYLOAD_CUSTOM_PLAN) -> (holder as? CustomSinglePlanViewHolder)?.payload(
                getItem(position)
            )
            else -> super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class SinglePlanCustomBtnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val et = itemView.findViewById<EditText>(R.id.et_custom_content)
        private val btnSend = itemView.findViewById<AppCompatImageView>(R.id.btn_send)

        fun bind(data: SinglePlanBean) {
            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.toString()?.isBlank() == false) {
                        setTint("#2196f3")
                    } else {
                        setTint("#9e9e9e")
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
            btnSend.setOnClickListener {
                val custom = et.text.toString()
                if (!custom.isBlank()) {
                    et.setText("")
                    listener?.onCustomClick(data.copy(content = custom))
                } else {
                    ToastUtil.toast("执行空计划，这个不大好吧~")
                }
            }
        }

        private fun setTint(colorRs: String) {
            val tintColor = Color.parseColor(colorRs)
            val sl = ColorStateList.valueOf(tintColor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnSend.imageTintList = sl
            } else {
                btnSend.drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    inner class CustomSinglePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_plan_content)
        private val btnDelete = itemView.findViewById<AppCompatImageView>(R.id.btn_close)

        fun bind(wrapper: SinglePlanBeanWrapper) {
            tvContent.text = wrapper.beanSingle.content
            updateStatus(wrapper)
        }

        fun payload(wrapper: SinglePlanBeanWrapper) {
            updateStatus(wrapper)
        }

        private fun updateStatus(wrapper: SinglePlanBeanWrapper) {
            if (wrapper.statusSingle == SinglePlanStatus.DELETE) {
                btnDelete.setImageResource(R.drawable.vector_bg_delete_restore)
                tvContent.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            } else {
                btnDelete.setImageResource(R.drawable.vector_bg_delete)
                tvContent.paint.flags = 0
                tvContent.paint.flags = Paint.ANTI_ALIAS_FLAG
            }

            btnDelete.setOnClickListener {
                listener?.onDeleteSinglePlanClick(
                    wrapper.beanSingle,
                    wrapper.statusSingle != SinglePlanStatus.DELETE
                )
            }
        }
    }

    inner class SinglePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnDelete = itemView.findViewById<AppCompatImageView>(R.id.btn_close)
        private val btnEdit = itemView.findViewById<AppCompatImageView>(R.id.btn_edit)
        private val etContent = itemView.findViewById<EditText>(R.id.tv_plan_content)

        fun bind(wrapper: SinglePlanBeanWrapper) {
            etContent.setText("${wrapper.beanSingle.content}\n${wrapper.beanSingle.factor}")
            updateStatus(wrapper)
            setListener(wrapper)
        }

        fun payload(wrapper: SinglePlanBeanWrapper) {
            updateStatus(wrapper)
            setListener(wrapper)
        }

        fun payloadContent(wrapper: SinglePlanBeanWrapper) {
            etContent.setText(wrapper.beanSingle.content)
            enableSend("#000000")
            etContent.clearFocus()
            setListener(wrapper)
        }

        private fun updateStatus(wrapper: SinglePlanBeanWrapper) {
            etContent.clearFocus()
            if (wrapper.statusSingle == SinglePlanStatus.DELETE) {
                btnDelete.setImageResource(R.drawable.vector_bg_delete_restore)
                etContent.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                etContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
            } else {
                btnDelete.setImageResource(R.drawable.vector_bg_delete)
                etContent.paint.flags = 0
                etContent.paint.flags = Paint.ANTI_ALIAS_FLAG
                etContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }
        }

        private fun setListener(wrapper: SinglePlanBeanWrapper) {
            btnDelete.setOnClickListener {
                listener?.onDeleteSinglePlanClick(
                    wrapper.beanSingle,
                    wrapper.statusSingle != SinglePlanStatus.DELETE
                )
            }

            btnEdit.setOnClickListener {
                etContent.requestFocus()
                val editStr = etContent.text.toString()
                if (!editStr.isBlank() && editStr != wrapper.beanSingle.content) {
                    listener?.onEditSinglePlan(wrapper.beanSingle, editStr)
                } else {
                    ToastUtil.toast("执行空计划不大好吧~")
                }
            }

            etContent.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.toString() != wrapper.beanSingle.content) {
                        enableSend("#2196f3")
                    } else {
                        enableSend("#9e9e9e")
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }

        private fun enableSend(colorRs: String) {
            val tintColor = Color.parseColor(colorRs)
            val sl = ColorStateList.valueOf(tintColor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnEdit.imageTintList = sl
            } else {
                btnEdit.drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}

interface OnSinglePlanClickListener {
    // true表示删除，false表示恢复
    fun onDeleteSinglePlanClick(data: SinglePlanBean, delete: Boolean)

    fun onCustomClick(data: SinglePlanBean)

    fun onEditSinglePlan(data: SinglePlanBean, editStr: String)
}