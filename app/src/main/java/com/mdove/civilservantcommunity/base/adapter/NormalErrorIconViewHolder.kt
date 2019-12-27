package com.mdove.civilservantcommunity.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R

/**
 * Created by zhaojing on 2019-12-27.
 */
const val TYPE_NORMAL_ERROR_ICON = 10000
const val TYPE_NORMAL_ERROR_TITLE = 10001

data class NormalErrorIconBean(val name: String = "error")

class NormalErrorIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NormalErrorTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

fun ViewGroup.createNormalErrorIconViewHolder(): NormalErrorIconViewHolder {
    return NormalErrorIconViewHolder(
        LayoutInflater.from(this.context).inflate(
            R.layout.item_normal_error_icon, this, false
        )
    )
}

fun ViewGroup.createNormalErrorTitleViewHolder(title: String): NormalErrorTitleViewHolder {
    return NormalErrorTitleViewHolder(
        LayoutInflater.from(this.context).inflate(
            R.layout.item_normal_error_title, this, false
        )
    ).apply {
        itemView.findViewById<TextView>(R.id.tv_title).text = title
    }
}

