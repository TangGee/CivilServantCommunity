package com.mdove.civilservantcommunity.plan.adapter

import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.plan.SinglePlanBeanWrapper
import com.mdove.civilservantcommunity.plan.SinglePlanType
import com.mdove.dependent.common.toast.ToastUtil

class EditSinglePlanAdapter(private val listener: OnSinglePlanClickListener? = null) :
    ListAdapter<SinglePlanBeanWrapper, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<SinglePlanBeanWrapper>() {
        override fun areContentsTheSame(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Boolean {
            return oldItem.beanSingle.content == oldItem.beanSingle.content
        }

        override fun areItemsTheSame(
            oldItem: SinglePlanBeanWrapper,
            newItem: SinglePlanBeanWrapper
        ): Boolean {
            return oldItem.beanSingle.content == newItem.beanSingle.content
        }
    }) {

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
                    R.layout.item_edit_single_plan,
                    parent,
                    false
                )
            )
            2 -> return CustomSinglePlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_custom_single_plan,
                    parent,
                    false
                )
            )
            else -> return SinglePlanCustomBtnViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_edit_custom_single_et_plan,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SinglePlanViewHolder -> {
                holder.bind(data = getItem(position).beanSingle)
            }
            is CustomSinglePlanViewHolder -> {
                holder.bind(data = getItem(position).beanSingle)
            }
            is SinglePlanCustomBtnViewHolder -> {
                holder.bind(data = getItem(position).beanSingle)
            }
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
        fun bind(data: SinglePlanBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
            itemView.findViewById<AppCompatImageView>(R.id.btn_close).setOnClickListener {
                listener?.onDeleteSinglePlanClick(data)
            }
        }
    }

    inner class SinglePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SinglePlanBean) {
            itemView.findViewById<TextView>(R.id.tv_plan_content).text = data.content
            itemView.findViewById<AppCompatImageView>(R.id.btn_close).setOnClickListener {
                listener?.onDeleteSinglePlanClick(data)
            }
        }
    }
}

interface OnSinglePlanClickListener {
    fun onDeleteSinglePlanClick(data: SinglePlanBean)
    fun onCustomClick(data: SinglePlanBean)
}