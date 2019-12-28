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
const val TYPE_NORMAL_NO_CONTENT_TITLE = 10002
const val TYPE_NORMAL_ADD_ARTICLE_TITLE = 10003
const val TYPE_NORMAL_TITLE = 10004

data class NormalErrorIconBean(val name: String = "error")

class NormalErrorIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NormalErrorTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NormalNoContentTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NormalAddArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NormalTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NormalTitleNoBgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

fun ViewGroup.createNormalTitleNoBgViewHolder(title: String): NormalTitleNoBgViewHolder {
    return NormalTitleNoBgViewHolder(
        LayoutInflater.from(this.context).inflate(
            R.layout.item_normal_title_no_bg, this, false
        )
    ).apply {
        itemView.findViewById<TextView>(R.id.tv_title).text = title
    }
}

fun ViewGroup.createNormalTitleViewHolder(title: String): NormalTitleViewHolder {
    return NormalTitleViewHolder(
        LayoutInflater.from(this.context).inflate(
            R.layout.item_normal_title, this, false
        )
    ).apply {
        itemView.findViewById<TextView>(R.id.tv_title).text = title
    }
}

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

fun ViewGroup.createNormalAddArticleViewHolder(
    title: String,
    listener: ViewHolderNormalClickListener? = null
): NormalAddArticleViewHolder {
    return NormalAddArticleViewHolder(
        LayoutInflater.from(this.context).inflate(
            R.layout.item_normal_add_article, this, false
        )
    ).apply {
        itemView.setOnClickListener {
            listener?.onClick()
        }
        itemView.findViewById<TextView>(R.id.tv_title).text = title
    }
}

fun ViewGroup.createNormalNoContentTitleViewHolder(title: String): NormalNoContentTitleViewHolder {
    return NormalNoContentTitleViewHolder(
        LayoutInflater.from(this.context).inflate(
            R.layout.item_normal_no_content_title, this, false
        )
    ).apply {
        itemView.findViewById<TextView>(R.id.tv_title).text = title
    }
}

interface ViewHolderNormalClickListener {
    fun onClick()
}

