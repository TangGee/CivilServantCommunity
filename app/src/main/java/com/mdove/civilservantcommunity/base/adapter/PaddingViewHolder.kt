package com.mdove.civilservantcommunity.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.dependent.common.utils.UIUtils

/**
 * Created by MDove on 2019-11-02.
 */
class HeightPaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class WidthPaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

fun ViewGroup.createHeightPadding(width: Float): RecyclerView.ViewHolder {
    val view =
        LayoutInflater.from(this.context).inflate(R.layout.item_rlv_padding_heght, this, false)
    view.layoutParams = view.layoutParams.apply {
        this.height = UIUtils.px2dip(this@createHeightPadding.context, width)
    }
    return HeightPaddingViewHolder(view)
}

fun ViewGroup.createWidthPadding(width: Float): RecyclerView.ViewHolder {
    val view =
        LayoutInflater.from(this.context).inflate(R.layout.item_rlv_padding_width, this, false)
    view.layoutParams = view.layoutParams.apply {
        this.width = UIUtils.px2dip(this@createWidthPadding.context, width)
    }
    return WidthPaddingViewHolder(view)
}
